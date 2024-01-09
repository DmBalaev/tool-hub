package dm.dm.toolservice.tool;

import dm.dm.toolservice.tool.dto.ToolDto;
import dm.dm.toolservice.tool.dto.ToolDtoCreate;
import dm.dm.toolservice.tool.dto.ToolDtoUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tool management")
@RequestMapping ("/api/v1/tools")
public class ToolController {
    private final ToolService toolService;

    @PostMapping
    @Operation(
            summary = "Create Tool",
            description = "Endpoint to create a new tool",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body for creating a tool",
                    content = @Content(schema = @Schema(implementation = ToolDtoCreate.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tool created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ToolDto.class)))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<ToolDto> saveTool(
            @Valid @RequestBody ToolDtoCreate request,
            @AuthenticationPrincipal UserDetails currentUser
            ){
        ToolDto toolDto = toolService.addNewTool(request, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(toolDto);
    }

    @PatchMapping("/{toolId}")
    @Operation(
            summary = "Update Tool",
            description = "Endpoint to update an existing tool",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool to be updated", in = ParameterIn.PATH, required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body for updating a tool",
                    content = @Content(schema = @Schema(implementation = ToolDtoUpdate.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tool updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ToolDto.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tool with the specified ID not found", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<ToolDto> updateTool(
            @PathVariable long toolId,
            @RequestBody ToolDtoUpdate request,
            @AuthenticationPrincipal UserDetails currentUser
            ){
        return ResponseEntity.ok(toolService.updateTool(toolId, request, currentUser));
    }

    @GetMapping("/{toolId}")
    @Operation(
            summary = "Search Tool",
            description = "Endpoint to retrieve details of a specific tool by its ID",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool to be retrieved", in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tool details retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ToolDto.class))),
                    @ApiResponse(responseCode = "404", description = "Tool with the specified ID not found", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<ToolDto> searchTool(@PathVariable long toolId){
        return ResponseEntity.ok(toolService.getById(toolId));
    }

    @GetMapping
    @Operation(
            summary = "Get Tools by Query",
            description = "Endpoint to retrieve a list of tools based on a search query",
            parameters = {
                    @Parameter(name = "page", description = "Page number (default: 0)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page (default: 10)", in = ParameterIn.QUERY),
                    @Parameter(name = "text", description = "Search text", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tools retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ToolDto.class))))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
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
    @Operation(
            summary = "Get Tools by Owner",
            description = "Endpoint to retrieve a list of tools owned by a specific user",
            parameters = {
                    @Parameter(name = "ownerId", description = "ID of the owner", in = ParameterIn.PATH, required = true),
                    @Parameter(name = "page", description = "Page number (default: 0)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page (default: 10)", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tools owned by the specified user retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ToolDto.class))))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public List<ToolDto> getToolsByOwner(
            @PathVariable long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());
        return toolService.getToolsByOwner(pageable, ownerId);
    }

    @DeleteMapping("/{toolId}")
    @Operation(
            summary = "Delete Tool",
            description = "Endpoint to delete a tool by its ID",
            parameters = {
                    @Parameter(name = "toolId", description = "ID of the tool to be deleted", in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "202", description = "Tool deleted successfully"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tool with the specified ID not found", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<?> deleteTool(
            @PathVariable long toolId,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        toolService.delete(toolId, currentUser);
        return ResponseEntity.accepted().body("Successfully");
    }
}
