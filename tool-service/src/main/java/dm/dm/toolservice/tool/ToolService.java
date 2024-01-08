package dm.dm.toolservice.tool;

import dm.dm.toolservice.tool.dto.ToolDto;
import dm.dm.toolservice.tool.dto.ToolDtoCreate;
import dm.dm.toolservice.tool.dto.ToolDtoUpdate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ToolService {
    @Query("SELECT i FROM Tool i " +
            "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "AND i.isAvailable = true")

    List<ToolDto> tools (String text, Pageable pageable);
    ToolDto getById(long id);
    ToolDto addNewTool(ToolDtoCreate dto, UserDetails userDetails);
    ToolDto updateTool(long idTool, ToolDtoUpdate dto, UserDetails userDetails);
    void delete(Long toolId, UserDetails userDetails);
    List<ToolDto> getToolsByOwner(Pageable pageable, long ownerId);
}
