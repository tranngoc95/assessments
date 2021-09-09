package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    private static final String SEED_PATH = "data/test_data/reservation-seed.csv";
    private static final String TEST_PATH =
            "data/test_data/reservation_test/3edda6bc-ab95-49a8-8962-d50b53f84b15.csv";

    private static final String TEST_DIRECTORY = "data/test_data/reservation_test";

    private static final String HOST_TEST_PATH = "data/test_data/host-seed.csv";
    private static final String GUEST_TEST_PATH = "data/test_data/guest-seed.csv";

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIRECTORY,
            new GuestFileRepository(GUEST_TEST_PATH), new HostFileRepository(HOST_TEST_PATH));

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByHostId(){
        List<Reservation> actual = repository.findByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertEquals(5, actual.size());
    }

    @Test
    void shouldFindUpcomingByHostId(){
        List<Reservation> actual = repository.findUpcomingByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertEquals(2, actual.size());
    }

    @Test
    void shouldFindUpcomingByGuestId(){
        List<Reservation> actual = repository.findUpcomingByGuestID(2);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindUpcomingByHostAndGuest() {
        List<Reservation> actual = repository.findUpcomingByHostAndGuest("3edda6bc-ab95-49a8-8962-d50b53f84b15", 2);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldNotFindPastInUpcomingByHostAndGuest() {
        List<Reservation> actual = repository.findUpcomingByHostAndGuest("3edda6bc-ab95-49a8-8962-d50b53f84b15", 5);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldFindByHostAndGuest() {
        List<Reservation> actual = repository.findByHostAndGuest("3edda6bc-ab95-49a8-8962-d50b53f84b15", 5);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindAllFive(){
        List<Reservation> actual = repository.findAll();
        assertEquals(5, actual.size());
    }

    @Test
    void shouldFindByGuestId(){
        List<Reservation> actual = repository.findByGuestId(1);
        assertEquals(1, actual.size());
        assertEquals(LocalDate.parse("2021-07-31"), actual.get(0).getStartDate());
    }

    @Test
    void shouldFindByState() {
        List<Reservation> actual = repository.findByState(State.TX);
        assertEquals(5, actual.size());
    }

    @Test
    void shouldFindByCity() {
        List<Reservation> actual = repository.findByCity("Amarillo");
        assertEquals(5, actual.size());
    }

    @Test
    void shouldFindByZipCode() {
        List<Reservation> actual = repository.findByZipCode(79182);
        assertEquals(5, actual.size());
    }

    @Test
    void shouldFindByID() throws DataException {
        Reservation actual = repository.findByID("1");
        assertNotNull(actual);
        assertEquals(1, actual.getGuest().getGuestID());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reserve = new Reservation();
        reserve.setHost(HostRepositoryDouble.HOST);
        reserve.setGuest(GuestRepositoryDouble.GUEST);
        reserve.setStartDate(LocalDate.now().plusDays(1));
        reserve.setEndDate(LocalDate.now().plusWeeks(1));
        reserve.updateTotal();
        Reservation actual = repository.add(reserve);
        assertNotNull(actual);
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation reserve = new Reservation();
        reserve.setReservationID("1");
        reserve.setHost(HostRepositoryDouble.HOST);
        reserve.setGuest(GuestRepositoryDouble.GUEST);
        reserve.setStartDate(LocalDate.parse("2021-07-31"));
        reserve.setEndDate(LocalDate.parse("2021-08-08"));
        reserve.updateTotal();
        assertTrue(repository.update(reserve));
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Reservation reserve = new Reservation();
        reserve.setReservationID("10");
        reserve.setHost(HostRepositoryDouble.HOST);
        reserve.setGuest(GuestRepositoryDouble.GUEST);
        reserve.setStartDate(LocalDate.parse("2022-07-31"));
        reserve.setEndDate(LocalDate.parse("2022-08-08"));
        reserve.updateTotal();
        assertFalse(repository.update(reserve));
    }

    @Test
    void shouldDelete() throws DataException {
        Reservation reserve = new Reservation();
        reserve.setReservationID("1");
        reserve.setHost(HostRepositoryDouble.HOST);
        reserve.setGuest(GuestRepositoryDouble.GUEST);
        reserve.setStartDate(LocalDate.parse("2021-07-31"));
        reserve.setEndDate(LocalDate.parse("2021-08-07"));
        reserve.updateTotal();
        assertTrue(repository.delete(reserve));
    }

    @Test
    void shouldNotDeleteMissing() throws DataException {
        Reservation reserve = new Reservation();
        reserve.setReservationID("10");
        reserve.setHost(HostRepositoryDouble.HOST);
        reserve.setGuest(GuestRepositoryDouble.GUEST);
        reserve.setStartDate(LocalDate.parse("2022-07-31"));
        reserve.setEndDate(LocalDate.parse("2022-08-08"));
        reserve.updateTotal();
        assertFalse(repository.delete(reserve));
    }
}