package dm.dm.toolservice.rental;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RentalRequest {
    private LocalDateTime start;
    private LocalDateTime end;
}
