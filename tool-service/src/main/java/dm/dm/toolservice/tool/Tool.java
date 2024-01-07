package dm.dm.toolservice.tool;

import dm.dm.toolservice.account.Account;
import dm.dm.toolservice.tool.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@Table(name = "tools")
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    @SequenceGenerator(
            name = "tool_sequence",
            sequenceName = "tool_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tool_sequence"
    )
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Account owner;
    private Boolean isAvailable;

    @Column(precision = 9, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;
}
