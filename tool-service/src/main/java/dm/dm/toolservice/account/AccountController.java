package dm.dm.toolservice.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountInfo>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<AccountInfo> getAccountByName(@PathVariable String username){
        return ResponseEntity.ok(accountService.getUserByName(username));
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountInfo> updateAccount(
            @PathVariable Long accountId,
            @RequestBody AccountUpdateRequest request,
            @AuthenticationPrincipal UserDetails currentUser
            ){
        return ResponseEntity.ok(accountService.updateAccount(accountId, request, currentUser));
    }
}
