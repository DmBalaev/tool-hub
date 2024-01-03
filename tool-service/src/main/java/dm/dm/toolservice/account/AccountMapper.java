package dm.dm.toolservice.account;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountMapper implements Function<Account,AccountInfo> {
    @Override
    public AccountInfo apply(Account account) {
        return AccountInfo.builder()
                .id(account.getId())
                .email(account.getEmail())
                .name(account.getName())
                .build();
    }
}
