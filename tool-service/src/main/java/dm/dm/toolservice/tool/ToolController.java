package dm.dm.toolservice.tool;

import dm.dm.toolservice.tool.dto.ToolDto;
import dm.dm.toolservice.tool.dto.ToolDtoCreate;
import dm.dm.toolservice.tool.dto.ToolDtoUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/v1/tools")
public class ToolController {
    private final ToolService toolService;

    @PostMapping
    public ResponseEntity<ToolDto> saveTool(
            @Valid @RequestBody ToolDtoCreate request,
            @AuthenticationPrincipal UserDetails currentUser
            ){
        ToolDto toolDto = toolService.addNewTool(request, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(toolDto);
    }

    @PatchMapping("/{toolId}")
    public ResponseEntity<ToolDto> updateTool(
            @PathVariable long toolId,
            @RequestBody ToolDtoUpdate request,
            @AuthenticationPrincipal UserDetails currentUser
            ){
        return ResponseEntity.ok(toolService.updateTool(toolId, request, currentUser));
    }

    @GetMapping("/{toolId}")
    public ResponseEntity<ToolDto> searchTool(@PathVariable long toolId){
        return ResponseEntity.ok(toolService.getById(toolId));
    }

    @GetMapping
    public List<ToolDto> getToolsByQuery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam() String text
            //Todo: добавить фильтрацию и сортировку
    ){
        Pageable pageable = PageRequest.of(page,size);
        return toolService.tools(text, pageable);
    }

    @GetMapping("/owner/{ownerId}")
    public List<ToolDto> getToolsByOwner(
            @PathVariable long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());
        return toolService.getToolsByOwner(pageable, ownerId);
    }

    @DeleteMapping("/{toolId}")
    public ResponseEntity<?> deleteTool(
            @PathVariable long toolId,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        toolService.delete(toolId, currentUser);
        return ResponseEntity.accepted().body("Succefuly");
    }

}
