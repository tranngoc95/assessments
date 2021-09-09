package learn.solarpanel.data;

import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolarFileRepositoriesTest {

    static final String SEED_PATH = "./data/solar-panels-seed.csv";
    static final String TEST_PATH = "./data/solar-panels-test.csv";

    SolarFileRepositories repositories = new SolarFileRepositories(TEST_PATH);

    @BeforeEach
    void setup() throws IOException{
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    // CREATE
    @Test
    void shouldAddPanel() throws DataAccessException {
        SolarPanel panel = new SolarPanel(4,"Rose",4,3,1999,MaterialType.CDTE,false);
        SolarPanel actual = repositories.add(panel);
        assertEquals(4, repositories.findAll().size());
        assertEquals(4, actual.getSolarID());
    }


    // READ
    @Test
    void shouldFindAll() throws DataAccessException {
        List<SolarPanel> actual = repositories.findAll();
        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldFindByExistingSection() throws DataAccessException{
        List<SolarPanel> actual = repositories.findBySection("Sunflower");
        assertNotNull(actual);
        assertEquals(1,actual.size());
    }

    @Test
    void shouldNotFindByMissingSection() throws DataAccessException{
        List<SolarPanel> actual = repositories.findBySection("Lily");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldFindOneExisting() throws DataAccessException{
        SolarPanel actual = repositories.findOne("Jasmine", 3, 3);
        assertNotNull(actual);
        assertEquals(3, actual.getSolarID());
    }

    @Test
    void shouldNotFindOneMissing() throws DataAccessException{
        SolarPanel actual = repositories.findOne("Lily", 3, 3);
        assertNull(actual);
    }

    @Test
    void shouldFindByExistingYearRange() throws DataAccessException{
        List<SolarPanel> actual = repositories.findByYearRange(2010, 2021);
        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotFindByMissingYearRange() throws DataAccessException{
        List<SolarPanel> actual = repositories.findByYearRange(2000, 2009);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldFindByExistingMaterial() throws DataAccessException{
        List<SolarPanel> actual = repositories.findByMaterial(MaterialType.CIGS);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldNotFindByMissingMaterial() throws DataAccessException{
        List<SolarPanel> actual = repositories.findByMaterial(MaterialType.ASI);
        assertEquals(0, actual.size());
    }

    // UPDATE
    @Test
    void shouldUpdateExisting() throws DataAccessException{
        boolean actual = repositories.update(new SolarPanel(1,"Orchids",
                2,3,2014,MaterialType.CDTE,true));
        assertTrue(actual);
        assertNotNull(repositories.findOne("Orchids", 2,3));
    }

    @Test
    void shouldNotUpdateMissing() throws DataAccessException{
        boolean actual = repositories.update(new SolarPanel(100, "Jasmine", 10, 10, 2000, MaterialType.CDTE, false));
        assertFalse(actual);
        assertNull(repositories.findOne("Jasmine",10,10));
    }

    // DELETE
    @Test
    void shouldDeleteExisting() throws DataAccessException{
        boolean actual = repositories.delete("Sunflower",1,1);
        assertTrue(actual);
        assertNull(repositories.findOne("Sunflower",1,1));
    }

    @Test
    void shouldNotDeleteMissing() throws DataAccessException{
        boolean actual = repositories.delete("Jasmine", 100,100);
        assertFalse(actual);
        assertEquals(3, repositories.findAll().size());
    }

}