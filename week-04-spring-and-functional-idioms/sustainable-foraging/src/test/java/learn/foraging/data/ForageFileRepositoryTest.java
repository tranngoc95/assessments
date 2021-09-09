package learn.foraging.data;

import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForageFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/forage-seed-2020-06-26.csv";
    static final String TEST_FILE_PATH = "./data/forage_data_test/2020-06-26.csv";
    static final String TEST_DIR_PATH = "./data/forage_data_test";

    static final String ITEM_PATH = "data/item_test/items-seed.txt";
    private final String FORAGER_PATH = "data/forager_test/forager-seed.csv";
    static final int FORAGE_COUNT = 54;

    final LocalDate date = LocalDate.of(2020, 6, 26);

    ForageFileRepository repository = new ForageFileRepository(TEST_DIR_PATH,
            new ItemFileRepository(ITEM_PATH), new ForagerFileRepository(FORAGER_PATH));

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByDate() {
        List<Forage> forages = repository.findByDate(date);
        assertEquals(FORAGE_COUNT, forages.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(date);
        forage.setKilograms(0.75);

        Item item = new Item(1,"Ramps", Category.EDIBLE,new BigDecimal("5.00"));
        forage.setItem(item);

        Forager forager = new Forager();
        forager.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");
        forager.setFirstName("Jilly");
        forager.setLastName("Sisse");
        forager.setState("GA");
        forage.setForager(forager);

        forage = repository.add(forage);

        assertEquals(36, forage.getId().length());
    }

}