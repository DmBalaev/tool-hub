package dm.dm.toolservice.rental;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByRenterAccount_Id(Long accountId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.renterAccount.id = :accountId AND r.status = 'PENDING'")
    List<Rental> findAllByRenterAccountAndFilterWaiting(@Param("accountId") Long accountId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.renterAccount.id = :accountId AND r.status = 'ACTIVE'")
    List<Rental> findAllByRenterAccountAndFilterCurrent(@Param("accountId") Long accountId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.renterAccount.id = :accountId AND r.status = 'COMPLETED'")
    List<Rental> findAllByRenterAccountAndFilterCompleted(@Param("accountId") Long accountId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.renterAccount.id = :accountId AND r.status = 'REJECTED'")
    List<Rental> findAllByRenterAccountAndFilterRejected(@Param("accountId") Long accountId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.renterAccount.id = :accountId AND (r.status = 'PENDING' OR r.status = 'ACTIVE' OR r.status = 'COMPLETED' OR r.status = 'REJECTED')")
    List<Rental> findAllByRenterAccountAndFilterAll(@Param("accountId") Long accountId, Pageable pageable);


    @Query("SELECT r FROM Rental r WHERE r.tool.owner.id = :ownerId")
    List<Rental> findAllByToolOwner(@Param("ownerId") Long ownerId, Pageable pageable);

    // Дополнительные запросы для фильтрации
    @Query("SELECT r FROM Rental r WHERE r.tool.owner.id = :ownerId AND r.status = 'PENDING'")
    List<Rental> findAllByToolOwnerAndFilterWaiting(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.tool.owner.id = :ownerId AND r.status = 'ACTIVE'")
    List<Rental> findAllByToolOwnerAndFilterCurrent(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.tool.owner.id = :ownerId AND r.status = 'COMPLETED'")
    List<Rental> findAllByToolOwnerAndFilterCompleted(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.tool.owner.id = :ownerId AND r.status = 'REJECTED'")
    List<Rental> findAllByToolOwnerAndFilterRejected(@Param("ownerId") Long ownerId, Pageable pageable);

}



