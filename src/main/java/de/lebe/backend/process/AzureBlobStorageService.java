package de.lebe.backend.process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.common.StorageSharedKeyCredential;

import de.lebe.backend.domain.LeBeFileAttachment;

@Service
public class AzureBlobStorageService {

	private final BlobContainerClient containerClient;

    public AzureBlobStorageService(
            @Value("${spring.cloud.azure.storage.blob.account-name}") String accountName,
            @Value("${spring.cloud.azure.storage.blob.account-key}") String accountKey,
            @Value("${spring.cloud.azure.storage.blob.endpoint}") String endpoint) {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();

        this.containerClient = blobServiceClient.getBlobContainerClient("privatecustomer");
    }


    public List<LeBeFileAttachment> getFilesForEmail(String email) {
        List<LeBeFileAttachment> attachments = new ArrayList<>();
        // 🔹 Alle Dateien im angegebenen Prefix abrufen
        for (BlobItem blobItem : containerClient.listBlobsByHierarchy("/", new ListBlobsOptions().setPrefix(email), null)) {
        	var blobClient = containerClient.getBlobClient(blobItem.getName());

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                blobClient.downloadStream(outputStream);

                LeBeFileAttachment attachment = new LeBeFileAttachment(blobItem.getName()
                		.replace(email +";", ""), outputStream.toByteArray());
                attachments.add(attachment);
            } catch (IOException e) {
                throw new RuntimeException("Fehler beim Laden der Datei: " + blobItem.getName(), e);
            }
        }

        return attachments;
    }
}
