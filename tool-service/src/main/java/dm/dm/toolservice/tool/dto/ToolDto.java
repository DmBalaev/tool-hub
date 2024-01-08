package dm.dm.toolservice.tool.dto;

import dm.dm.toolservice.tool.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ToolDto {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Category category;
}
