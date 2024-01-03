package dm.dm.toolservice.account;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AccountService {
    AccountInfo getUserByName(String username);
    List<AccountInfo> getAllUsers();
    AccountInfo updateAccount(long accountId, AccountUpdateRequest dto, UserDetails currentUser);

}
