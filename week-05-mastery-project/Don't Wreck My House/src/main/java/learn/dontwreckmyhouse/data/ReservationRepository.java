package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    List<Reservation> findByHostId(String hostID);

    List<Reservation> findByGuestId(int guestID);

    List<Reservation> findByState(State state);

    List<Reservation> findByCity(String city);

    List<Reservation> findByZipCode(int zipCode);

    List<Reservation> findByHostAndGuest(String hostID, int guestID);

    List<Reservation> findUpcomingByHostId(String hostID);

    List<Reservation> findUpcomingByGuestID(int guestID);

    List<Reservation> findUpcomingByHostAndGuest(String hostID, int guestID);

    Reservation findByID(String reservationID);

    Reservation add(Reservation reservation) throws DataException;

    boolean update(Reservation reservation) throws DataException;

    boolean delete(Reservation reservation) throws DataException;

}
