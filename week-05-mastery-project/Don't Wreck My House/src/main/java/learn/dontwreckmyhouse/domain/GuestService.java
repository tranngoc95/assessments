package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.State;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestService {
    private final GuestRepository repository;

    public GuestService(GuestRepository repository){
        this.repository=repository;
    }

    public List<Guest> findByLastName(String lastName) throws DataException {
        return repository.findByLastNameActive(lastName).stream()
                .sorted(Comparator.comparing(Guest::getLastName))
                .collect(Collectors.toList());
    }

    public List<Guest> findByState(State state) throws DataException {
        return repository.findByStateActive(state);
    }

    public Guest findById(int guestId) throws DataException {
        return repository.findByIdAll(guestId);
    }

    public Guest findByEmail(String email) throws DataException {
        return repository.findByEmailActive(email);
    }

    public Result<Guest> add(Guest guest) throws DataException {
        Result<Guest> result = validate(guest, true);

        if(!result.isSuccess()){
            return result;
        }

        result.setPayLoad(repository.add(guest));

        return result;
    }

    public Result<Guest> update(Guest guest) throws DataException {
        Result<Guest> result = validate(guest, false);

        if(!result.isSuccess()){
            return result;
        }

        if(!repository.update(guest)){
            result.addErrorMessage("Cannot find Guest " + guest.getLastName());
        }

        result.setPayLoad(guest);

        return result;
    }

    public Result<Guest> delete(Guest guest) throws DataException {
        Result<Guest> result = new Result<>();

        if(!repository.delete(guest)){
            result.addErrorMessage("Cannot find Guest " + guest.getLastName());
        }

        result.setPayLoad(guest);

        return result;
    }

    private Result<Guest> validate(Guest guest, boolean isAdd) throws DataException {
        Result<Guest> result = new Result<>();

        if(guest==null){
            result.addErrorMessage("Guest must not be null.");
            return result;
        }

        if(guest.getLastName()==null || guest.getLastName().isBlank()){
            result.addErrorMessage("Guest's last name is required.");
        }

        if(guest.getFirstName()==null || guest.getFirstName().isBlank()){
            result.addErrorMessage("Guest's first name is required.");
        }

        if(guest.getEmail()==null || guest.getEmail().isBlank()){
            result.addErrorMessage("Guest's email is required.");
        } else {
            Guest guestFound = findByEmail(guest.getEmail());
            if (guestFound!=null && (isAdd || guestFound.getGuestID()!=guest.getGuestID())){
                result.addErrorMessage("Guest's email already exist.");
            }
        }

        if(guest.getPhoneNumber()==null || guest.getPhoneNumber().isBlank()){
            result.addErrorMessage("Guest's phone number is required.");
        }

        if(guest.getState()==null){
            result.addErrorMessage("Guest's state is required.");
        }

        return result;
    }
}
