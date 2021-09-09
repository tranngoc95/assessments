package learn.solarpanel.data;

import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SolarJDBCTemplateRepositoryTest {

    @Autowired
    SolarJDBCTemplateRepository repository;

    @Autowired
    JdbcTemplate template;

    private static boolean hasRunPreviously = false;

    @BeforeEach
    public void init() {
        if(!hasRunPreviously) {
            template.update("call set_known_good_state();");
            hasRunPreviously = true;
        }
    }

    @Test
    void shouldFindAll() throws DataAccessException {
        List<SolarPanel> all = repository.findAll();

        assertNotNull(all);
        assertTrue(all.size() >= 2);

        SolarPanel expected = new SolarPanel();
        expected.setSolarID(1);
        expected.setSection("Sunflower");
        expected.setRow(1);
        expected.setCol(1);
        expected.setYearInstalled(2014);
        expected.setMaterial(MaterialType.CIGS);
        expected.setTracking(true);

        assertTrue(all.contains(expected));
        assertTrue(all.stream().anyMatch(i -> i.getSolarID() == 2));
    }

    @Test
    void shouldFindOne() throws DataAccessException {
        SolarPanel expected = new SolarPanel();
        expected.setSolarID(1);
        expected.setSection("Sunflower");
        expected.setRow(1);
        expected.setCol(1);
        expected.setYearInstalled(2014);
        expected.setMaterial(MaterialType.CIGS);
        expected.setTracking(true);

        assertEquals(expected, repository.findOne("Sunflower",1,1));

    }

    @Test
    void shouldAdd() throws DataAccessException {
        SolarPanel panel = new SolarPanel();
        panel.setSection("Orchid");
        panel.setRow(4);
        panel.setCol(5);
        panel.setYearInstalled(2010);
        panel.setMaterial(MaterialType.ASI);
        panel.setTracking(false);

        SolarPanel actual = repository.add(panel);
        actual.setSolarID(4);

        assertNotNull(actual);
        assertEquals(panel, actual);
    }

    @Test
    void shouldUpdateExisting() throws DataAccessException {
        SolarPanel panel = new SolarPanel();
        panel.setSolarID(2);
        panel.setSection("Rose");
        panel.setRow(2);
        panel.setCol(3);
        panel.setYearInstalled(2018);
        panel.setMaterial(MaterialType.GIGS);
        panel.setTracking(true);

        assertTrue(repository.update(panel));
    }

    @Test
    void shouldNotUpdateMissing() throws DataAccessException {
        SolarPanel panel = new SolarPanel();
        panel.setSolarID(2000);
        panel.setSection("Orchid");
        panel.setRow(2);
        panel.setCol(3);
        panel.setYearInstalled(2018);
        panel.setMaterial(MaterialType.GIGS);
        panel.setTracking(false);

        assertFalse(repository.update(panel));
    }

    @Test
    void shouldDeleteExisting() throws DataAccessException {
        assertTrue(repository.delete("Jasmine", 3, 3));
    }

    @Test
    void shouldNotDeleteMissing() throws DataAccessException {
        assertFalse(repository.delete("Jasmine", 100, 100));
    }
}