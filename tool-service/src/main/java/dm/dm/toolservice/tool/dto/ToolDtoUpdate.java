package dm.dm.toolservice.tool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToolDtoUpdate {
    private String name;
    private Double price;
    private String description;
}
