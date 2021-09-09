package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(new ReservationRepositoryDouble(),
            new GuestRepositoryDouble(), new HostRepositoryDouble());

    // READ
    @Test
    void shouldFindByHost(){
        List<Reservation> actual = service.findByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindByGuest(){
        List<Reservation> actual = service.findByGuestId(1);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindByState(){
        List<Reservation> actual = service.findByState(State.TX);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindByCity(){
        List<Reservation> actual = service.findByCity("Amarillo");
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindUpcomingByHostAndGuest() {
        List<Reservation> actual = service.findUpcomingByHostAndGuest("3edda6bc-ab95-49a8-8962-d50b53f84b15", 1);
        assertEquals(2, actual.size());
    }

    @Test
    void shouldFindUpcomingByHostID(){
        List<Reservation> actual = service.findUpcomingByHostID("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertEquals(2, actual.size());
    }

    @Test
    void shouldFindUpcomingByGuestID(){
        List<Reservation> actual = service.findUpcomingByGuestID(1);
        assertEquals(2, actual.size());
    }

    @Test
    void shouldFindByZipCode(){
        List<Reservation> actual = service.findByZipCode(79182);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindByHostAndGuest(){
        List<Reservation> actual = service.findByHostAndGuest("3edda6bc-ab95-49a8-8962-d50b53f84b15", 1);
        assertEquals(3, actual.size());
    }

    // CREATE
    @Test
    void shouldNotAddNull() throws DataException {
        Result<Reservation> actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("empty"));

    }

    @Test
    void shouldNotAddEmptyGuest() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(2));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(null);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("guest"));
    }

    @Test
    void shouldNotAddEmptyHost() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(2));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(null);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("host"));
    }

    @Test
    void shouldNotAddEmptyStartDate() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(null);
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("start date"));
    }

    @Test
    void shouldNotAddEmptyEndDate() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(2));
        one.setEndDate(null);
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("end date"));
    }

    @Test
    void shouldNotAddNonExistingGuest() throws DataException {
        Guest guest = new Guest();
        guest.setGuestID(100);

        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(2));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(guest);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("guest"));
    }

    @Test
    void shouldNotAddNonExistingHost() throws DataException {
        Host host = new Host();
        host.setHostID("b6ddb844-b990-471a-8c0a-519d0777eb9b");
        host.setStandardRate(BigDecimal.ONE);
        host.setWeekendRate(BigDecimal.TEN);

        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(2));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(host);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("host"));

    }

    @Test
    void shouldNotAddPastDate() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.of(2021,1,1));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("start date"));
    }

    @Test
    void shouldNotAddStartDateIsAfterEndDate() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(3));
        one.setEndDate(LocalDate.now().plusWeeks(2));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("start date"));
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("end date"));
    }

    @Test
    void shouldNotAddOverlapDate() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusDays(2));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("overlap"));
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation one = new Reservation();
        one.setStartDate(LocalDate.now().plusWeeks(2));
        one.setEndDate(LocalDate.now().plusWeeks(3));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.add(one);
        assertTrue(actual.isSuccess());
    }

    // UPDATE
    @Test
    void shouldNotUpdateNull() throws DataException {
        Result<Reservation> actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("empty"));
    }

    @Test
    void shouldNotUpdateEmptyGuest() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(null);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("guest"));
    }

    @Test
    void shouldNotUpdateEmptyHost() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(null);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("host"));
    }

    @Test
    void shouldNotUpdateEmptyStartDate() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(null);
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("start date"));
    }

    @Test
    void shouldNotUpdateEmptyEndDate() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(null);
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("end date"));
    }

    @Test
    void shouldNotUpdateNotExistingGuest() throws DataException {
        Guest guest = new Guest();
        guest.setGuestID(100);

        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(guest);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("guest"));
    }

    @Test
    void shouldNotUpdateNonExistingHost() throws DataException {
        Host host = new Host();
        host.setHostID("b6ddb844-b990-471a-8c0a-519d0777eb9b");
        host.setStandardRate(BigDecimal.ONE);
        host.setWeekendRate(BigDecimal.TEN);

        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(host);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("host"));
    }

    @Test
    void shouldNotUpdateStartDateIsAfterEndDate() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusWeeks(3));
        one.setEndDate(LocalDate.now().plusWeeks(2));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("start date"));
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("end date"));
    }

    @Test
    void shouldNotUpdateOverlap() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusWeeks(3));
        one.setEndDate(one.getStartDate().plusMonths(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("overlap"));
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("100");
        one.setStartDate(LocalDate.now().plusMonths(5));
        one.setEndDate(one.getStartDate().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("reservation"));
    }

    @Test
    void shouldNotUpdateFutureToPast() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.of(2021,1,1));
        one.setEndDate(LocalDate.of(2021,1,22));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("upcoming"));

    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.update(one);
        assertTrue(actual.isSuccess());
    }

    // DELETE
    @Test
    void shouldNotDeleteMissing() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("100");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.delete(one);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("reservation"));
    }

    @Test
    void shouldNotDeletePastReservation() throws DataException {
        Reservation three = new Reservation();
        three.setReservationID("3");
        three.setStartDate(LocalDate.of(2021,1,1));
        three.setEndDate(three.getStartDate().plusWeeks(2));
        three.setGuest(GuestRepositoryDouble.GUEST);
        three.setHost(HostRepositoryDouble.HOST);
        three.updateTotal();

        Result<Reservation> actual = service.delete(three);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("past"));
    }

    @Test
    void shouldDelete() throws DataException {
        Reservation one = new Reservation();
        one.setReservationID("1");
        one.setStartDate(LocalDate.now().plusDays(3));
        one.setEndDate(LocalDate.now().plusWeeks(1));
        one.setGuest(GuestRepositoryDouble.GUEST);
        one.setHost(HostRepositoryDouble.HOST);
        one.updateTotal();

        Result<Reservation> actual = service.delete(one);
        assertTrue(actual.isSuccess());
    }


}