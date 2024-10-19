package br.com.edielsonassis.bookstore.dtos.v1.request;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Represents a book")
public class BookUpdateRequest implements Serializable {
    
	@Schema(description = "Unique identifier of the book", example = "1", required = true)
    @NotNull(message = "BookId date is required")
    private Long bookId;

    @Schema(description = "Name of the author", example = "Robert C. Martin", required = true)
	@NotBlank(message = "Author is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'.]+$", message = "Only letters should be typed")
    private String author;

    @Schema(description = "The date the book was launched", example = "2008-08-01", required = true)
	@NotNull(message = "LaunchDate is required")
	@PastOrPresent(message = "Invalid date")
    private LocalDate launchDate;

    @Schema(description = "Title of the book", example = "Clean Code: A Handbook of Agile Software Craftsmanship", required = true)
    @NotBlank(message = "Title is required")
	private String title;

    @Schema(description = "Description of the book", example = "A book that teaches software developers how to write clean, maintainable, and efficient code.", required = true)
    @NotBlank(message = "Description is required")
	private String description;
}