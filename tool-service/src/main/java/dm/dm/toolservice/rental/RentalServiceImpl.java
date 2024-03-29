package dm.dm.toolservice.rental;

import dm.dm.toolservice.account.Account;
import dm.dm.toolservice.account.AccountRepository;
import dm.dm.toolservice.exception.*;
import dm.dm.toolservice.tool.Tool;
import dm.dm.toolservice.tool.ToolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final ToolRepository toolRepository;
    private final AccountRepository accountRepository;
    private final RentalMapper rentalMapper;

    @Override
    public RentalResponse rent(Long toolId, RentalRequest request, UserDetails currentUser) {
        var tool = getTool(toolId);
        var account = getAccount(currentUser.getUsername());

        if (!tool.getIsAvailable()) {
            throw new ToolNotAvailableException("Tool id not available");
        }
        if (tool.getOwner().getEmail().equals(currentUser.getUsername())) {
            throw new UnauthorizedUserException("Account already owns this tool.");
        }

        var rental = Rental.builder()
                .tool(tool)
                .renter(account)
                .startDate(request.getStart())
                .endDate(request.getEnd())
                .status(RentalStatus.PENDING)
                .build();
        tool.setIsAvailable(false);

        rentalRepository.save(rental);
        toolRepository.save(tool);
        log.info("RentalService: Account {} want rent the tool with id {}", currentUser.getUsername(), toolId);
        //Todo: добавить логику уведомления
        return rentalMapper.apply(rental);
    }

    @Override
    public RentalResponse approve(Long rentalId, Boolean isApproved, UserDetails currentUser) {
        var rental = getRental(rentalId);

        if (!rental.getTool().getOwner().getEmail().equals(currentUser.getUsername())) {
            throw new UnauthorizedUserException("Only the owner of the item can confirm/reject the reservation");
        }
        if (!rental.getStatus().equals(RentalStatus.PENDING)) {
            throw new IllegalRentalStatusException("The reservation status does not allow confirmation/rejection");
        }
        var newRentalStatus = isApproved ? RentalStatus.ACTIVE : RentalStatus.CANCELED;
        rental.setStatus(newRentalStatus);
        rentalRepository.save(rental);

        log.info("RentalService: {} has {} the rental with ID: {}. New status: {}",
                currentUser.getUsername(),
                isApproved ? "approved" : "rejected",
                rentalId,
                newRentalStatus);

        return rentalMapper.apply(rental);
    }

    @Override
    public RentalResponse returnRental(Long rentalId, UserDetails userDetails) {
        var rental = getRental(rentalId);

        if (!rental.getStatus().equals(RentalStatus.ACTIVE)) {
            throw new IllegalRentalStatusException("The rental status does not allow return");
        }
        if (!rental.getTool().getOwner().getEmail().equals(userDetails.getUsername())) {
            throw new UnauthorizedUserException("Only the owner of the tool can close the rental");
        }
        rental.setStatus(RentalStatus.COMPLETED);
        rental.getTool().setIsAvailable(true);

        rentalRepository.save(rental);
        //toolRepository.save(rental.getTool()); проверить нужна ли
        log.info("RentalService: Rental with id {} has been closed by owner {}. Tool with id {} is now available",
                rentalId, userDetails.getUsername(), rental.getTool().getId());
        return rentalMapper.apply(rental);
    }

    @Override
    @Transactional(readOnly = true)
    public RentalResponse getRentalById(Long rentalId, UserDetails currentUser) {
        log.info("RentalService: Account with name {} gets rental with id {}", currentUser.getUsername(), rentalId);
        var rental = getRental(rentalId);

        if (!rental.getRenter().getEmail().equals(currentUser.getUsername()) ||
                rental.getTool().getOwner().getEmail().equals(currentUser.getUsername())) {
            throw new UnauthorizedUserException("Only the author or owner can view");
        }

        return rentalRepository.findById(rentalId)
                .map(rentalMapper)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Rental with id %s not found", rentalId)));
    }

    //Todo: getRentalsByOwner и getMyRentals подумать над неймингом методов
    @Override
    @Transactional(readOnly = true)
    public List<RentalResponse> getRentalsByOwner(Pageable pageable, String filter, UserDetails currentUser) {
        var account = getAccount(currentUser.getUsername());
        RentalFilter rentalFilter;
        try {
            rentalFilter = RentalFilter.valueOf(filter);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown filter");
        }
        List<Rental> rentals;
        switch (rentalFilter) {
            case ALL -> rentals =  rentalRepository.findAllByToolOwner(account.getId(), pageable);
            case CURRENT -> rentals = rentalRepository.findAllByToolOwnerCurrent(account.getId(), pageable);
            case COMPLETED -> rentals = rentalRepository.findAllByToolOwnerAndStatus(account.getId(),RentalStatus.COMPLETED, pageable);
            case REJECTED -> rentals = rentalRepository.findAllByToolOwnerAndStatus(account.getId(), RentalStatus.CANCELED, pageable);
            default -> throw new UnsupportedStatusException("Unknown filter");
        }

        return rentals.stream()
                .map(rentalMapper)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalResponse> getMyRentals(Pageable pageable, String filter, UserDetails currentUser) {
        log.info("Rental service: {} call method get my rentals", currentUser.getUsername());
        var account = getAccount(currentUser.getUsername());
        RentalFilter rentalFilter;
        try {
            rentalFilter = RentalFilter.valueOf(filter.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown filter");
        }
        List<Rental> rentals;
        switch (rentalFilter) {
            case ALL -> rentals =  rentalRepository.findAllByRenterId(account.getId(), pageable);
            case CURRENT -> rentals = rentalRepository.findCurrentRentals(account.getId(), pageable);
            case COMPLETED -> rentals = rentalRepository.findAllByRenterIdAndStatus(account.getId(),RentalStatus.COMPLETED, pageable);
            case REJECTED -> rentals = rentalRepository.findAllByRenterIdAndStatus(account.getId(), RentalStatus.CANCELED, pageable);                           //findAllByRenterAccountAndFilterRejected(account.getId(), pageable);
            default -> throw new UnsupportedStatusException("Unknown filter");
        }
        assert rentals != null;
        return rentals.stream()
                .map(rentalMapper)
                .toList();
    }

    private Account getAccount(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with name: %s not found", email)));
    }

    private Rental getRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
    }

    private Tool getTool(Long id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Tool with id:%s not found", id)));
    }
}