package dm.dm.toolservice.rental;

import dm.dm.toolservice.account.Account;
import dm.dm.toolservice.tool.Tool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "rentals")
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @SequenceGenerator(
            name = "rental_sequence",
            sequenceName = "rental_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rental_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "renter_account_id", nullable = false)
    private Account renterAccount;

    @ManyToOne
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;
}
