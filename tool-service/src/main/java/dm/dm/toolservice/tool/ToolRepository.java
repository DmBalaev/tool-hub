package dm.dm.toolservice.tool;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    List<Tool> findAllByOwnerId(long ownerId, Pageable pageable);

    @Query("SELECT i FROM Tool i " +
            "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "AND i.isAvailable = true")
    List<Tool> search(String text, Pageable pageable);
}
