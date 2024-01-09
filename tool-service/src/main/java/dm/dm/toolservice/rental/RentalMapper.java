package dm.dm.toolservice.rental;

import dm.dm.toolservice.account.AccountMapper;
import dm.dm.toolservice.tool.dto.ToolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class RentalMapper implements Function<Rental,RentalResponse> {
    private final AccountMapper accountMapper;
    private final ToolMapper toolMapper;
    @Override
    public RentalResponse apply(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .start(rental.getStartDate())
                .end(rental.getEndDate())
                .renter(accountMapper.apply(rental.getRenter()))
                .tool(toolMapper.apply(rental.getTool()))
                .status(rental.getStatus())
                .build();
    }
}
