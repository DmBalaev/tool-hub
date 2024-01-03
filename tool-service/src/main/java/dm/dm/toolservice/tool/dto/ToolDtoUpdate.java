package dm.dm.toolservice.tool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ToolDtoUpdate {
    private String name;
    private BigDecimal price;
}
