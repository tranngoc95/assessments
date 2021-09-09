package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository repository;
    private final GuestRepository guestRepo;
    private final HostRepository hostRepo;

    public ReservationService(ReservationRepository repository,
                              GuestRepository guestRepo, HostRepository hostRepo){
        this.repository = repository;
        this.guestRepo = guestRepo;
        this.hostRepo = hostRepo;
    }

    public List<Reservation> findByHostId(String hostID){
        return repository.findByHostId(hostID).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByGuestId(int guestID){
        return repository.findByGuestId(guestID).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    public List<Reservation> findUpcomingByHostAndGuest(String hostID, int guestID){
        return repository.findUpcomingByHostAndGuest(hostID, guestID).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    public List<Reservation> findUpcomingByHostID(String hostID){
        return repository.findUpcomingByHostId(hostID).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    public List<Reservation> findUpcomingByGuestID(int guestID){
        return repository.findUpcomingByGuestID(guestID).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByState(State state){
        return repository.findByState(state).stream()
                .sorted((a,b)-> a.getStartDate().compareTo(b.getEndDate()))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByCity(String city){
        return repository.findByCity(city).stream()
                .sorted((a,b)-> a.getStartDate().compareTo(b.getEndDate()))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByZipCode(int zipCode){
        return repository.findByZipCode(zipCode).stream()
                .sorted((a,b)-> a.getStartDate().compareTo(b.getEndDate()))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByHostAndGuest(String hostID, int guestID){
        return repository.findByHostAndGuest(hostID, guestID).stream()
                .sorted((a,b)-> a.getStartDate().compareTo(b.getEndDate()))
                .collect(Collectors.toList());
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, true);

        if(result.isSuccess()){
            result.setPayLoad(repository.add(reservation));
        }

        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, false);

        if(result.isSuccess()) {
            if (!repository.update(reservation)) {
                result.addErrorMessage(
                        "Cannot find reservation on " + reservation.getStartDate()
                                + " for " + reservation.getGuest().getLastName());
            }
        }
        result.setPayLoad(reservation);
        return result;
    }

    public Result<Reservation> delete(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();

        if(reservation.getStartDate().isBefore(LocalDate.now())){
            result.addErrorMessage("Past reservations cannot be canceled.");
        }

        if(!repository.delete(reservation)){
            result.addErrorMessage(String.format("Cannot find reservation on %s for guest %s.",
                    reservation.getStartDate(), reservation.getGuest().getLastName()));
        }
        result.setPayLoad(reservation);
        return result;
    }

    // helper methods
    private Result<Reservation> validate(Reservation reservation, boolean isAdd) throws DataException {
        Result<Reservation> result = validateNull(reservation);
        if(!result.isSuccess()){
            return result;
        }
        validateFieldsExist(reservation, result);

        validateDates(reservation, result, isAdd);

        return result;
    }

    private Result<Reservation> validateNull(Reservation reservation){
        Result<Reservation> result = new Result<>();
        if(reservation==null){
            result.addErrorMessage("Reservation information cannot be empty.");
            return result;
        }

        if(reservation.getGuest()==null){
            result.addErrorMessage("Guest is required.");
        }

        if(reservation.getHost()==null){
            result.addErrorMessage("Host is required.");
        }

        if(reservation.getStartDate()==null){
            result.addErrorMessage("Start date is required.");
        }

        if(reservation.getEndDate()==null){
            result.addErrorMessage("End date is required.");
        }
        return result;
    }

    private void validateFieldsExist(Reservation reservation, Result<Reservation> result) throws DataException {
        if(reservation.getGuest().getGuestID() == 0 ||
                guestRepo.findByIdAll(reservation.getGuest().getGuestID()) == null){
            result.addErrorMessage("Guest does not exist.");
        }

        if(reservation.getHost().getHostID() == null ||
                hostRepo.findById(reservation.getHost().getHostID()) == null){
            result.addErrorMessage("Host does not exist.");
        }
    }

    private void validateDates(Reservation reservation, Result<Reservation> result, boolean isAdd){
        if(isAdd) {
            if (reservation.getStartDate().isBefore(LocalDate.now())) {
                result.addErrorMessage("Start date should be in future.");
            }
        } else {
            Reservation old = repository.findByID(reservation.getReservationID());
            if(old!=null && !old.getStartDate().isBefore(LocalDate.now())
                    && reservation.getStartDate().isBefore(LocalDate.now())){
                result.addErrorMessage("Cannot move an upcoming reservation to the past.");
            }
        }

        if(!reservation.getStartDate().isBefore(reservation.getEndDate())){
            result.addErrorMessage("Start date should come before end date.");
        }

        List<Reservation> future = repository.findUpcomingByHostId(reservation.getHost().getHostID());
        if(future!=null && future.size()!=0) {
            for (Reservation r : future) {
                if(!isAdd && r.getReservationID().equals(reservation.getReservationID())){
                    continue;
                }
                boolean startDateOverlap = !reservation.getStartDate().isBefore(r.getStartDate())
                        && reservation.getStartDate().isBefore(r.getEndDate());
                boolean endDateOverlap = reservation.getEndDate().isAfter(r.getStartDate())
                        && !reservation.getEndDate().isAfter(r.getEndDate());
                boolean coverOverlap = !reservation.getStartDate().isAfter(r.getStartDate())
                        && !reservation.getEndDate().isBefore(r.getEndDate());
                if (startDateOverlap || endDateOverlap || coverOverlap) {
                    result.addErrorMessage("Reservation overlaps with an existed reservation.");
                }
            }
        }
    }
}
