package br.com.edielsonassis.bookstore.dtos.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for file upload operation")
public record UploadFileResponse(

    @Schema(description = "Name of the uploaded file", example = "document.pdf") 
    String name,

    @Schema(description = "URI for downloading the file", example = "http://localhost:8080/api/v1/file/download/document.pdf") 
    String downloadUri,

    @Schema(description = "File type (MIME)", example = "application/pdf") 
    String type,

    @Schema(description = "Size of the file in bytes", example = "200") 
    long size
) {}