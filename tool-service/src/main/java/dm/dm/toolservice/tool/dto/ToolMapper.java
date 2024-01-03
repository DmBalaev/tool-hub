package dm.dm.toolservice.tool.dto;

import dm.dm.toolservice.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ToolMapper implements Function<Tool, ToolDto> {
    @Override
    public ToolDto apply(Tool tool) {
        return ToolDto.builder()
                .name(tool.getName())
               // .price(tool.getPrice())
                .build();
    }
}
