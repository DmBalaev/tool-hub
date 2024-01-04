package dm.dm.toolservice.rental;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RentalMapper implements Function<Rental,RentalResponse> {
    @Override
    public RentalResponse apply(Rental rental) {
        return null;
    }
}
