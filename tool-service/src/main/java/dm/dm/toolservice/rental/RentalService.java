package dm.dm.toolservice.rental;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface RentalService {

    RentalResponse rent(Long toolId, RentalRequest request, UserDetails currentUser);

    RentalResponse approve(Long rentalId, Boolean isApproved, UserDetails currentUser);

    RentalResponse returnRental(Long rentalId, UserDetails currentUser);

    RentalResponse getRentalById(Long rentalId, UserDetails currentUser);

    List<RentalResponse> getRentalsByOwner(Pageable pageable, String filter, UserDetails currentUser);
    List<RentalResponse> getMyRentals(Pageable pageable, String filter, UserDetails currentUser);
}
