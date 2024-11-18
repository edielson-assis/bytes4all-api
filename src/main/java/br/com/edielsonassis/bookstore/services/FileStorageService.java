package br.com.edielsonassis.bookstore.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.edielsonassis.bookstore.configs.FileStorageConfig;
import br.com.edielsonassis.bookstore.services.exceptions.FileNotFoundException;
import br.com.edielsonassis.bookstore.services.exceptions.FileStorageException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileStorageService {
    
    private final Path fileStorageLocation;

    private FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
        this.fileStorageLocation = path;
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("File storage directory created at {}", this.fileStorageLocation);
        } catch (Exception e) {
            log.error("Could not create file storage directory", e);
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (!filename.endsWith(".pdf")) {
            log.warn("Attempted upload of a non-PDF file: {}", filename);
            throw new FileStorageException("Only PDF files are accepted.");
        }

        try {
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File successfully stored: {}", filename);
            return filename;
        } catch (Exception e) {
            log.error("Failed to store file {}", filename, e);
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String filename) {
        if (!filename.endsWith(".pdf")) {
            filename = filename.concat(".pdf");
        }
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                log.warn("File not found: {}", filename);
                throw new FileNotFoundException("File not found: " + filename);
            }
            log.info("File loaded successfully: {}", filename);
            return resource;
        } catch (Exception e) {
            log.error("Failed to load file {}", filename);
            throw new FileNotFoundException("File not found: " + filename);
        }
    }
}