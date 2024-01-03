package dm.dm.toolservice.tool;

import dm.dm.toolservice.tool.dto.ToolDto;
import dm.dm.toolservice.tool.dto.ToolDtoCreate;
import dm.dm.toolservice.tool.dto.ToolDtoUpdate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ToolService {
    List<ToolDto> tools (Pageable pageable);
    ToolDto getById(long id);
    ToolDto addNewTool(ToolDtoCreate dto, UserDetails userDetails);
    ToolDto updateTool(long idTool, ToolDtoUpdate dto, UserDetails userDetails);
    void delete(Long toolId, UserDetails userDetails);
}
