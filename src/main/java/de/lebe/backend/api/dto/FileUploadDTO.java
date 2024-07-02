package de.lebe.backend.api.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class FileUploadDTO {

    @NotEmpty(message = "Die E-Mail darf nicht leer sein.")
    @Email(message = "Ungültige E-Mail-Adresse.")
    private String email;

    @NotNull(message = "Die Datei darf nicht leer sein.")
    private MultipartFile file;

    // Getter und Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
