package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private static final String SEED_PATH = "data/test_data/host-seed.csv";
    private static final String TEST_PATH = "data/test_data/host-test.csv";

    HostFileRepository repository = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    // READ
    @Test
    void shouldFindAllFive() throws DataException {
        assertEquals(5, repository.findAll().size());
    }

    @Test
    void shouldFindByLastName() throws DataException {
        List<Host> actual = repository.findByLastName("Harley");
        assertEquals(1, actual.size());
        assertEquals(State.FL, actual.get(0).getState());
    }

    @Test
    void shouldFindByState() throws DataException {
        List<Host> actual = repository.findByState(State.TX);
        assertEquals(2, actual.size());
        assertEquals("Yearnes", actual.get(0).getLastName());
    }

    @Test
    void shouldFindById() throws DataException {
        Host actual = repository.findById("b4f38829-c663-48fc-8bf3-7fca47a7ae70");
        assertNotNull(actual);
        assertEquals("Fader", actual.getLastName());
    }

    @Test
    void shouldFindByEmail() throws DataException {
        Host actual = repository.findByEmail("mfader2@amazon.co.jp");
        assertNotNull(actual);
        assertEquals("Fader", actual.getLastName());
    }
}