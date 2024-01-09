package dm.dm.toolservice.rental;

import dm.dm.toolservice.account.AccountInfo;
import dm.dm.toolservice.tool.dto.ToolDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ToolDto tool;
    private AccountInfo renter;
    private RentalStatus status;
}
