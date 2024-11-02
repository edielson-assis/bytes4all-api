package br.com.edielsonassis.bookstore.controllers.swagger;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.com.edielsonassis.bookstore.dtos.v1.response.UploadFileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File", description = "Operations related to file upload and download")
public interface FileControllerSwagger {

    static final String SECURITY_SCHEME_KEY = "bearer-key";
    
    @Operation(
        security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)},
        summary = "Upload multiple PDF files",
		description = "Allows uploading one or more PDF files",
		tags = {"File"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = UploadFileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - File not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<List<UploadFileResponse>> uploadMultipleFiles(MultipartFile[] files);

    @Operation(
        summary = "Download a file by filename",
		description = "Allows downloading a file based on its filename",
		tags = {"File"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = UploadFileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found - File not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<Resource> downloadFile(String filename);
}