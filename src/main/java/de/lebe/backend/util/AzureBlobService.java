package de.lebe.backend.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AzureBlobService {

	@Autowired
	private ResourceLoader resourceLoader;

	public void uploadFile(MultipartFile file) throws IOException {
		try {
			String blobPath = "azure-blob://[DeinContainer]/" + file.getOriginalFilename();
			WritableResource resource = (WritableResource) resourceLoader.getResource(blobPath);
			try (InputStream inputStream = file.getInputStream()) {
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