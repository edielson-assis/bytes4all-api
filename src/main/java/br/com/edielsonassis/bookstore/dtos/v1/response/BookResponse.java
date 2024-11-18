package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Represents a book")
public class BookResponse extends RepresentationModel<BookResponse> implements Serializable {
    
	@Schema(description = "Unique identifier of the book", example = "1")
    private Long bookId;

    @Schema(description = "Name of the author", example = "Robert C. Martin")
    private String author;

    @Schema(description = "The date the book was launched", example = "2008-08-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate launchDate;

    @Schema(description = "Title of the book", example = "Clean Code: A Handbook of Agile Software Craftsmanship")
    private String title;

    @Schema(description = "Description of the book", example = "A book that teaches software developers how to write clean, maintainable, and efficient code.")
    private String description;

    @Schema(description = "PDF files", example = "http://localhost/api/v1/books/download/test.pdf")
    private String downloadUrl;
}