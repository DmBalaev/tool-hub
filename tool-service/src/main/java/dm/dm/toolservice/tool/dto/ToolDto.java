package dm.dm.toolservice.tool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ToolDto {
    private String name;
    private BigDecimal price;
}
