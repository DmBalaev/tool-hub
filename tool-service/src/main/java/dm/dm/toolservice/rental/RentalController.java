package dm.dm.toolservice.rental;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
@Tag(name = "Rental management")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping("/{toolId}")
    @Operation(
            summary = "Rent Tool",
            description = "Endpoint to rent a tool by its ID",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool to be rented", in = ParameterIn.PATH, required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body for renting a tool",
                    content = @Content(schema = @Schema(implementation = RentalRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tool rented successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tool with the specified ID not found", content = @Content),
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<RentalResponse> rentTool(
            @PathVariable long toolId,
            @RequestBody RentalRequest request,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.rent(toolId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PatchMapping("/{toolId}")
    @Operation(
            summary = "Approve Rental",
            description = "Endpoint to approve or reject a rental request for a tool by its ID",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool rental to be approved or rejected", in = ParameterIn.PATH, required = true),
                    @Parameter(name = "approved", description = "Boolean indicating approval or rejection", in = ParameterIn.QUERY, schema = @Schema(type = "boolean"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental request approved or rejected successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tool rental with the specified ID not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict - Rental request is already approved or rejected", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<RentalResponse> approve(
            @PathVariable long toolId,
            @RequestParam(name = "approved") Boolean isApproved,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.approve(toolId, isApproved, currentUser);
        return ResponseEntity.ok(rental);
    }

    @PatchMapping("/return/{toolId}")
    @Operation(
            summary = "Return Rental",
            description = "Endpoint to mark a rented tool as returned by its ID",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool rental to be marked as returned", in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental marked as returned successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tool rental with the specified ID not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict - Rental is already marked as returned", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<RentalResponse> returnRental(
            @PathVariable long toolId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.returnRental(toolId, currentUser);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/{toolId}")
    @Operation(
            summary = "Get Rental by ID",
            description = "Endpoint to retrieve details of a rental by its ID",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool rental to be retrieved", in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental details retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tool rental with the specified ID not found", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<RentalResponse> getRentalById(
            @PathVariable long toolId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.returnRental(toolId, currentUser);
        return ResponseEntity.ok(rental);
    }

    @GetMapping
    @Operation(
            summary = "Get All Rentals for Renter",
            description = "Endpoint to retrieve all rentals for the current renter account",
            parameters = {
                    @Parameter(name = "page", description = "Page number (default: 0)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page (default: 10)", in = ParameterIn.QUERY),
                    @Parameter(name = "filter", description = "Filter for rentals (default: ALL)", in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rentals retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RentalResponse.class))))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<List<RentalResponse>> getAllRenterAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(name = "filter", defaultValue = "ALL") String filter,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        var rental = rentalService.getMyRentals(pageable, filter, currentUser);

        return ResponseEntity.ok(rental);
    }

    @GetMapping("/owner")
    @Operation(
            summary = "Get All Rentals by Owner",
            description = "Endpoint to retrieve all rentals associated with the current owner account",
            parameters = {
                    @Parameter(name = "page", description = "Page number (default: 0)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page (default: 10)", in = ParameterIn.QUERY),
                    @Parameter(name = "filter", description = "Filter for rentals (default: ALL)", in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rentals retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RentalResponse.class))))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<List<RentalResponse>> getAllByOwner(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(name = "filter", defaultValue = "ALL") String filter,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        var rental = rentalService.getRentalsByOwner(pageable, filter, currentUser);

        return ResponseEntity.ok(rental);
    }
}