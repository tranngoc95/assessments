package learn.foraging.data;

import learn.foraging.models.Forager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerFileRepositoryTest {

    private final String SEED_PATH = "data/forager_test/forager-seed.csv";
    private final String TEST_PATH = "data/forager_test/forager-test.csv";

    ForagerFileRepository repo = new ForagerFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Forager> all = repo.findAll();
        assertEquals(10, all.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Rachel");
        forager.setLastName("Green");
        forager.setState("NY");
        repo.add(forager);
        assertEquals(11, repo.findAll().size());
        assertEquals(36, forager.getId().length());
    }
}