package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private ArrayList<Reservation> all = new ArrayList<>();

    public ReservationRepositoryDouble(){
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(1));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Reservation two = new Reservation();
        two.setReservationID("2");
        two.setStartDate(LocalDate.now().plusMonths(1));
        two.setEndDate(two.getStartDate().plusWeeks(2));
        two.setGuest(GuestRepositoryDouble.GUEST);
        two.setHost(HostRepositoryDouble.HOST);
        two.updateTotal();

        Reservation three = new Reservation();
        three.setReservationID("3");
        three.setStartDate(LocalDate.of(2021,1,1));
        three.setEndDate(three.getStartDate().plusWeeks(2));
        three.setGuest(GuestRepositoryDouble.GUEST);
        three.setHost(HostRepositoryDouble.HOST);
        three.updateTotal();

        all.add(one);
        all.add(two);
        all.add(three);
    }

    @Override
    public List<Reservation> findAll() {
        return all;
    }

    @Override
    public List<Reservation> findByHostId(String hostID) {
        return Arrays.asList(all.get(0));
    }

    @Override
    public List<Reservation> findByGuestId(int guestID) {
        return guestID==1 ? Arrays.asList(all.get(0)) : null;
    }

    @Override
    public List<Reservation> findByState(State state) {
        return Arrays.asList(all.get(0));
    }

    @Override
    public List<Reservation> findByCity(String city) {
        return Arrays.asList(all.get(0));
    }

    @Override
    public List<Reservation> findByZipCode(int zipCode) {
        return Arrays.asList(all.get(0));
    }

    @Override
    public List<Reservation> findUpcomingByHostId(String hostID) {
        return Arrays.asList(all.get(0), all.get(1));
    }

    @Override
    public List<Reservation> findUpcomingByHostAndGuest(String hostID, int guestID) {
        return Arrays.asList(all.get(0), all.get(1));
    }

    @Override
    public List<Reservation> findUpcomingByGuestID(int guestID) {
        return Arrays.asList(all.get(0), all.get(1));
    }

    @Override
    public List<Reservation> findByHostAndGuest(String hostID, int guestID) {
        return all;
    }

    @Override
    public Reservation findByID(String reservationID) {
        return all.stream()
                .filter(r->r.getReservationID().equals(reservationID))
                .findFirst().orElse(null);
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        if(reservation==null){
            return false;
        }
        return reservation.getReservationID().equals(all.get(0).getReservationID());
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        return reservation.getReservationID().equals(all.get(0).getReservationID());
    }
}
