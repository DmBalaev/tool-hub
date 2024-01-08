package dm.dm.toolservice.tool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToolDtoCreate {
    private String name;
    private String description;
    private Double price;
    private String category;
}
