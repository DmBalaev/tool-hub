package dm.dm.toolservice.tool;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    @Id
    private Long id;
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_id")
    private Account owner;
    private boolean isAvailable;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;
    //TODO добавить характеристики HashMap
}
