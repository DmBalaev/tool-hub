package dm.dm.toolservice.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    private Long id;
    private String name;
    private String email;
}
