package dm.dm.toolservice.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    @Test
    public void apply_ShouldConvectAccountToAccountInfo(){
     AccountMapper accountMapper = new AccountMapper();
     Account account = Account.builder()
             .id(1L)
             .name("Test")
             .email("test@email.com")
             .build();

     AccountInfo accountInfo = accountMapper.apply(account);

     assertEquals(accountInfo.getId(), account.getId());
     assertEquals(accountInfo.getEmail(), account.getEmail());
     assertEquals(accountInfo.getName(), account.getName());
    }
}