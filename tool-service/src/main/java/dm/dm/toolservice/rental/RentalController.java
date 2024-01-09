package dm.dm.toolservice.rental;

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
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping("/{toolId}")
    public ResponseEntity<RentalResponse> rentTool(
            @PathVariable long toolId,
            @RequestBody RentalRequest request,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.rent(toolId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PatchMapping("/{toolId}")
    public ResponseEntity<RentalResponse> approve(
            @PathVariable long toolId,
            @RequestParam(name = "approved") Boolean isApproved,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.approve(toolId, isApproved, currentUser);
        return ResponseEntity.ok(rental);
    }

    @PatchMapping("/return/{toolId}")
    public ResponseEntity<RentalResponse> returnRental(
            @PathVariable long toolId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.returnRental(toolId, currentUser);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/{toolId}")
    public ResponseEntity<RentalResponse> getRentalById(
            @PathVariable long toolId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var rental = rentalService.returnRental(toolId, currentUser);
        return ResponseEntity.ok(rental);
    }

    @GetMapping
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
