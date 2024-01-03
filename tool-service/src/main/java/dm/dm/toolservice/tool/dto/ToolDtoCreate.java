package dm.dm.toolservice.tool.dto;

import dm.dm.toolservice.tool.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ToolDtoCreate {
    private String name;
    private BigDecimal price;
    private Category category;
}
