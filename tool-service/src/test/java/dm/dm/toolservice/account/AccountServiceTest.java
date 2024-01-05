package dm.dm.toolservice.account;

import dm.dm.toolservice.exception.EntityNotFoundException;
import dm.dm.toolservice.exception.NotHavePermissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountServiceImpl accountService;
    private Account first;
    private Account second;
    private AccountInfo accountInfoFirst;
    private AccountInfo accountInfoSecond;
    private static final String EMAIL = "test@email.com";

    @BeforeEach
    public void init() {
        first = Account.builder()
                .id(1L)
                .email("test@email.com")
                .name("Test")
                .build();

        second = Account.builder()
                .id(2L)
                .email("asdasd@email.com")
                .name("Test2")
                .build();

        accountInfoFirst = AccountInfo.builder()
                .id(1L)
                .name("Test")
                .email("test@email.com")
                .build();

        accountInfoSecond = AccountInfo.builder()
                .id(2L)
                .email("asdasd@email.com")
                .name("Test2")
                .build();
    }

    @Test
    public void getUserByName_withCorrectName_ReturnsAccountInfo(){
        when(accountRepository.findByEmail(EMAIL)).thenReturn(Optional.of(first));
        when(accountMapper.apply(first)).thenReturn(accountInfoFirst);
        AccountInfo actual = accountService.getUserByName(EMAIL);

        assertEquals(accountInfoFirst, actual);
        verify(accountRepository, times(1)).findByEmail(EMAIL);
        verify(accountMapper, times(1)).apply(first);
    }

    @Test
    public void getUserByName_withInCorrectName_thenThrowException(){
        when(accountRepository.findByEmail("BAD_EMAIL")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.getUserByName("BAD_EMAIL"));
        verify(accountRepository, times(1)).findByEmail("BAD_EMAIL");
    }

    @Test
    public void updateAccount_NotOwner_thenThrowException(){
        when(accountRepository.findById(first.getId())).thenReturn(Optional.of(first));

        assertThrows(NotHavePermissions.class, () -> accountService.updateAccount(first.getId(), any(AccountUpdateRequest.class), second));
    }

    @Test
    public void updateAccount_Successful_ReturnsAccountInfo(){
        AccountUpdateRequest request = new AccountUpdateRequest("newName", "newPass");
        AccountInfo expected = new AccountInfo(1L, "newName", "newPass");

        when(accountRepository.findById(first.getId())).thenReturn(Optional.of(first));
        when(accountRepository.save(first)).thenReturn(first);
        when(accountMapper.apply(first)).thenReturn(expected);

        AccountInfo actual = accountService.updateAccount(first.getId(), request, first);

        assertEquals(expected.getName() ,actual.getName());
        verify(accountRepository, times(1)).save(first);
    }

    @Test
    public void getAllUsers(){
        List<Account> accounts = List.of(first, second);
        List<AccountInfo> expected = List.of(accountInfoFirst, accountInfoSecond);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountInfo> actual = accountService.getAllUsers();

        verify(accountRepository, times(1)).findAll();
        assertEquals(expected.size(), actual.size());
    }
}