package dm.dm.toolservice.account;

import dm.dm.toolservice.exception.EntityNotFoundException;
import dm.dm.toolservice.exception.NotHavePermissions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    private final AccountMapper accountConvector;
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public AccountInfo getUserByName(String name) {
        log.info("Getting a user by ID:{}", name);
        return accountRepository.findByEmail(name)
                .map(accountConvector)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Account with name: %s not found", name)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountInfo> getAllUsers() {
        log.info("Getting all users");
        return accountRepository.findAll().stream()
                .map(accountConvector)
                .toList();
    }

    @Override
    public AccountInfo updateAccount(long accountId, AccountUpdateRequest dto, UserDetails currentUser) {
        log.info("Update account with id:{}", accountId);
        var account = accountRepository.findById(accountId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Account with name: %s not found", accountId)));

        if (!account.getEmail().equals(currentUser.getUsername())){
            throw new NotHavePermissions("You can't update account, you are not the owner");
        }

        if(dto.getName() != null){
            account.setName(dto.getName());
        }
        if (dto.getPassword() != null){
            account.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return accountConvector.apply(accountRepository.save(account));
    }

}