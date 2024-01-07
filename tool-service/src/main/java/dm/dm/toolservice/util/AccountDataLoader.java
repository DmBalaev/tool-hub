package dm.dm.toolservice.util;

import dm.dm.toolservice.account.Account;
import dm.dm.toolservice.account.AccountRepository;
import dm.dm.toolservice.account.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountDataLoader {
    private final AccountRepository accountRepository;

    private final static HashMap<String, String> NAMES_AND_EMAILS = new HashMap<>();
    static {
        NAMES_AND_EMAILS.put("John", "john@example.com");
        NAMES_AND_EMAILS.put("Jane", "jane@example.com");
        NAMES_AND_EMAILS.put("Alice", "alice@example.com");
        NAMES_AND_EMAILS.put("Bob", "bob@example.com");
        NAMES_AND_EMAILS.put("Eva", "eva@example.com");
    }

    @Bean
    public CommandLineRunner addAccounts(){
        return args -> {
            log.info("Load account...");
            List<Account> accounts = new ArrayList<>();
            for (String name : NAMES_AND_EMAILS.keySet()){
                accounts.add(Account.builder()
                                .name(name)
                                .email(NAMES_AND_EMAILS.get(name))
                                .role(Role.USER)
                                .password("password")
                        .build());
            }

            accountRepository.saveAll(accounts);
        };
    }
}
