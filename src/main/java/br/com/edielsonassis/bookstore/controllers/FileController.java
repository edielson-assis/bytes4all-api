package br.com.edielsonassis.bookstore.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.edielsonassis.bookstore.controllers.swagger.FileControllerSwagger;
import br.com.edielsonassis.bookstore.dtos.v1.response.UploadFileResponse;
import br.com.edielsonassis.bookstore.services.FileStorageService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/file")
public class FileController implements FileControllerSwagger {

    private final FileStorageService service;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<UploadFileResponse>> uploadMultipleFiles(@RequestPart("files") MultipartFile[] files) {
        var list = Arrays.stream(files).map(file -> uploadFile(file)).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = service.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) 
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    private UploadFileResponse uploadFile(MultipartFile file) {
        String filename = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/file/download/").path(filename).toUriString();
        return new UploadFileResponse(filename, fileDownloadUri, file.getContentType(), file.getSize());
    }
}