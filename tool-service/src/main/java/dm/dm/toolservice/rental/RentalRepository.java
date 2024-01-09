package dm.dm.toolservice.rental;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByRenterId(Long renterAccountId, Pageable pageable);

    @Query("""
        SELECT r FROM Rental r 
        WHERE r.renter.id = ?1
        AND r.status = ?2
        """)
    List<Rental> findAllByRenterIdAndStatus(long renterId, RentalStatus status, Pageable pageable);

    @Query("""
        SELECT r FROM Rental r 
        WHERE r.renter.id = ?1
        AND current timestamp BETWEEN r.startDate AND r.endDate
        """)
    List<Rental> findCurrentRentals(long renterId, Pageable pageable);

    @Query("""
        SELECT r FROM Rental r
        WHERE r.tool.owner.id = ?1
        """)
    List<Rental> findAllByToolOwner(long ownerId, Pageable pageable);

    @Query("""
        SELECT r FROM Rental r 
        WHERE r.tool.owner.id = ?1
        AND current timestamp BETWEEN r.startDate AND r.endDate
        """)
    List<Rental> findAllByToolOwnerCurrent(long ownerId, Pageable pageable);

    @Query("""
        SELECT r FROM Rental r 
        WHERE r.tool.owner.id = ?1
        AND r.status = ?2
        """)
    List<Rental> findAllByToolOwnerAndStatus(long ownerId, RentalStatus status, Pageable pageable);
}