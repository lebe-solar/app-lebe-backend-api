package de.lebe.backend.process;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import de.lebe.backend.api.dto.FileUploadDTO;

@Service
public class FileUploadProcess {
	
	@Autowired
    private ResourceLoader resourceLoader;

	public void handleFileUpload(FileUploadDTO fileUploadDTO) {
		try {
			String safeEmail = fileUploadDTO.getEmail();// .replace("@", "_at_").replace(".", "_dot_");
			String blobPath = "azure-blob://privatecustomer/" + safeEmail + ";"
					+ fileUploadDTO.getFile().getOriginalFilename();
			WritableResource resource = (WritableResource) resourceLoader.getResource(blobPath);
			try (InputStream inputStream = fileUploadDTO.getFile().getInputStream()) {
				try (OutputStream os = resource.getOutputStream()) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
