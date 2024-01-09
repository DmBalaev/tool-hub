package dm.dm.toolservice.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account management")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @Operation(
            tags = "Account management",
            summary = "Return all user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all users"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    public ResponseEntity<List<AccountInfo>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllUsers());
    }

    @Operation(
            tags = "Account management",
            summary = "Return user by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get user "),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "The user with the same id does not exist ", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @GetMapping("/{username}")
    public ResponseEntity<AccountInfo> getAccountByName(@PathVariable String username){
        return ResponseEntity.ok(accountService.getUserByName(username));
    }

    @Operation(
            tags = "Account management",
            summary = "Update user account",
            description = "Update the user account information by providing the account ID and the request body with the updated details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User account updated successfully"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User account with the specified ID not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountInfo> updateAccount(
            @PathVariable Long accountId,
            @RequestBody AccountUpdateRequest request,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        return ResponseEntity.ok(accountService.updateAccount(accountId, request, currentUser));
    }
}




