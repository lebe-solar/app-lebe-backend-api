package de.lebe.backend.api;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import de.lebe.backend.api.dto.CommonResponse;
import de.lebe.backend.api.dto.FileUploadDTO;
import de.lebe.backend.process.FileUploadProcess;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Controller
public class FileUploadController {

	private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	@Value("${solar.apikey}")
	String apiKey;
	
	private static final List<String> ALLOWED_TYPES = Arrays.asList(
	        "application/pdf", // PDF
	        "image/jpeg",      // JPEG
	        "image/png"        // PNG
	    );
	
	private static AutoDetectParser parser = new AutoDetectParser(TikaConfig.getDefaultConfig());
	private static BodyContentHandler handler = new BodyContentHandler();
	
    @Autowired
    private FileUploadProcess process;
    
    @Autowired
    private Validator validator;
    
    @GetMapping("/advisor/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("fileUploadDTO", new FileUploadDTO()); // Stelle sicher, dass `FileUploadDTO` das Feld `email` enthält
        return "upload";
    }
    
    @PostMapping("/customer/upload")
    @ResponseBody
    public CommonResponse customerFileUpload(@RequestHeader("x-api-key") String code,
    		@ModelAttribute FileUploadDTO fileUploadDTO) {
    	
    	if(!apiKey.equals(code)) {
    		throw new AccessDeniedException("Invalid apikey");
    	}
        
    	Set<ConstraintViolation<FileUploadDTO>> violations = validator.validate(fileUploadDTO);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<FileUploadDTO> violation : violations) {
                errors.append(violation.getMessage()).append("\n");
            }
            logger.error("Invalid request from {} due to {}", fileUploadDTO.getEmail(), errors.toString());
            throw new IllegalArgumentException(errors.toString());
        }
        
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, fileUploadDTO.getFile().getContentType()); // Setzt den Content-Type direkt aus der Datei
        metadata.add("resourceName", fileUploadDTO.getFile().getOriginalFilename());
        
        try {
            parser.parse(fileUploadDTO.getFile().getInputStream(), handler, metadata, new ParseContext());
            String detectedType = metadata.get(Metadata.CONTENT_TYPE);
            if(!ALLOWED_TYPES.contains(detectedType)) {
            	logger.error("Invalid file from {} - {}", fileUploadDTO.getEmail(), fileUploadDTO.getFile().getOriginalFilename());
            	throw new IllegalArgumentException("Ungültiges Dateiformat");
            }
        } catch (IOException | SAXException | TikaException e) {
        	logger.error("Invalid file from {} - {} due to {}", fileUploadDTO.getEmail(), fileUploadDTO.getFile().getOriginalFilename(),
        			e.getMessage());
        	throw new IllegalArgumentException("Die Datei konnte nicht hochgeladen werden. Bitte versuchen Sie es später erneut.");
        }
        
        process.handleFileUpload(fileUploadDTO);
        logger.info("Upload file {} from {} completed", fileUploadDTO.getEmail(), fileUploadDTO.getFile().getOriginalFilename());
        return new CommonResponse("200",
				"Dateiupload erfolgreich");
    	
    }
    
    @PostMapping("/advisor/upload")
    public String handleFileUpload(@ModelAttribute @Valid FileUploadDTO fileUploadDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Fehler: " + result.getFieldError().getDefaultMessage());
            return "redirect:/advisor/upload";
        }

        try {
        	process.handleFileUpload(fileUploadDTO);
            redirectAttributes.addFlashAttribute("message", "Datei erfolgreich hochgeladen: " + fileUploadDTO.getFile().getOriginalFilename());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Hochladen der Datei: " + e.getMessage());
        }

        return "redirect:/advisor/upload";
    }
}
