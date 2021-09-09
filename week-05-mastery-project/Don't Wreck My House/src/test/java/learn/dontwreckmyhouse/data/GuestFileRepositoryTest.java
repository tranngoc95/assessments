package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {
    private static final String SEED_PATH = "data/test_data/guest-seed.csv";
    private static final String TEST_PATH = "data/test_data/guest-test.csv";

    GuestFileRepository repository = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }


    // READ
    @Test
    void shouldFindAllSix() throws DataException {
        assertEquals(6, repository.findAll().size());
    }

    @Test
    void shouldFindActive() throws DataException {
        assertEquals(5, repository.findActive().size());
    }

    @Test
    void shouldFindByLastNameActive() throws DataException {
        List<Guest> actual = repository.findByLastNameActive("Gecks");
        assertEquals(1, actual.size());
        assertEquals(2, actual.get(0).getGuestID());
    }

    @Test
    void shouldFindByState() throws DataException {
        List<Guest> actual = repository.findByStateActive(State.DC);
        assertEquals(2, actual.size());
        assertEquals(2, actual.get(0).getGuestID());
        assertEquals(5, actual.get(1).getGuestID());
    }

    @Test
    void shouldFindById() throws DataException {
        Guest actual = repository.findByIdAll(3);
        assertNotNull(actual);
        assertEquals("Tremain", actual.getFirstName());
    }

    @Test
    void shouldFindByEmailActive() throws DataException {
        Guest actual = repository.findByEmailActive("bseppey4@yahoo.com");
        assertNotNull(actual);
        assertEquals("Berta", actual.getFirstName());
    }

    // CREATE
    @Test
    void shouldAdd() throws DataException {
        Guest guest = new Guest(0, "Rachel", "Green",
                "rachelgreen@gmail.com", "(123) 4567890", State.NY);
        repository.add(guest);
        assertEquals(7, repository.findAll().size());
        assertEquals("Rachel", repository.findByIdAll(7).getFirstName());
    }

    // UPDATE
    @Test
    void shouldUpdateExisting() throws DataException {
        Guest guest = new Guest(1,"Sullivan","Lomas",
                "slomas0@mediafire.com","(702) 7768761",State.ND);
        assertTrue(repository.update(guest));
        assertEquals(State.ND, repository.findByIdAll(1).getState());
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Guest guest = new Guest(7, "Rachel", "Green",
                "rachelgreen@gmail.com", "(123) 4567890", State.NY);
        assertFalse(repository.update(guest));
    }

    // DELETE
    @Test
    void shouldDeleteExisting() throws DataException {
        Guest guest = new Guest(1,"Sullivan","Lomas",
                "slomas0@mediafire.com","(702) 7768761",State.NV);
        assertTrue(repository.delete(guest));
        assertNull(repository.findByIdActive(1));
    }

    @Test
    void shouldNotDeleteMissing() throws DataException {
        Guest guest = new Guest(7, "Rachel", "Green",
                "rachelgreen@gmail.com", "(123) 4567890", State.NY);
        assertFalse(repository.delete(guest));
    }
}