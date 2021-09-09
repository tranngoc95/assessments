package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    public static final Guest GUEST = new Guest(1,"Sullivan","Lomas","slomas0@mediafire.com","(702) 7768761",State.NV);
    public static final Guest GUEST1 = new Guest(2,"Olympie","Gecks","ogecks1@dagondesign.com","(202) 2528316",State.DC);
    private List<Guest> all = new ArrayList<Guest>();

    public GuestRepositoryDouble(){
        all.add(GUEST);
        all.add(GUEST1);
    }

    @Override
    public List<Guest> findAll() throws DataException {
        return all;
    }

    @Override
    public List<Guest> findByLastNameActive(String lastName) throws DataException {
        return Arrays.asList(GUEST);
    }

    @Override
    public List<Guest> findByStateActive(State state) throws DataException {
        return Arrays.asList(GUEST);
    }

    @Override
    public Guest findByEmailActive(String email) throws DataException {
        return all.stream().filter(g->g.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    @Override
    public Guest findByIdAll(int guestId) throws DataException {
        return all.stream().filter(g->g.getGuestID()==guestId).findFirst().orElse(null);
    }

    @Override
    public List<Guest> findActive() throws DataException {
        return all;
    }

    @Override
    public Guest findByIdActive(int guestId) throws DataException {
        return GUEST;
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        return guest;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        return guest.getGuestID()==1;
    }

    @Override
    public boolean delete(Guest guest) throws DataException {
        return guest.getGuestID()==1;
    }
}
