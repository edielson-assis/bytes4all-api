package br.com.edielsonassis.bookstore.controllers.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "People", description = "Endpoints for Managing People")
public interface PersonControllerSwagger {
    
    @Operation(
        summary = "Adds a new Person",
		description = "Adds a new Person by passing in a JSON, XML or YML representation of the person!",
		tags = {"People"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Created person",
				content = @Content(schema = @Schema(implementation = PersonResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<PersonResponse> createPerson(PersonRequest personRequest);

    @Operation(
        summary = "Finds a Person",
		description = "Finds a person by their Id",
		tags = {"People"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = PersonResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Person not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<PersonResponse> findPersonById(@Parameter(description = "The Id of the person to find.") Long id);

    @Operation(
        summary = "Finds all People", description = "Finds all People",
		tags = {"People"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<List<PersonResponse>> findAllPeople();

    @Operation(
        summary = "Updates a Person",
		description = "Updates a Person by passing in a JSON, XML or YML representation of the person!",
		tags = {"People"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Updated person", 
				content = @Content(schema = @Schema(implementation = PersonResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Person not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<PersonResponse> updatePerson(PersonUpdateRequest personRequest);

    @Operation(
        summary = "Deletes a Person",
		description = "Deletes a Person by their Id",
		tags = {"People"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Deleted person", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Person not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<Void> deletePerson(@Parameter(description = "The Id of the person to find.") Long id);
}