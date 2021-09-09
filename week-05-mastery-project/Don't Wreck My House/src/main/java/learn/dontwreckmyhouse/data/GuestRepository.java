package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.State;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll() throws DataException;
    List<Guest> findActive() throws DataException;
    List<Guest> findByLastNameActive(String lastName) throws DataException;
    List<Guest> findByStateActive(State state) throws DataException;
    Guest findByEmailActive(String email) throws DataException;
    Guest findByIdAll(int guestId) throws DataException;
    Guest findByIdActive(int guestId) throws DataException;
    Guest add(Guest guest) throws DataException;
    boolean update(Guest guest) throws DataException;
    boolean delete(Guest guest) throws DataException;
}
