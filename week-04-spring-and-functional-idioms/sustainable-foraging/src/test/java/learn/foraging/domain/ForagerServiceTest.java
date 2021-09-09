package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Forager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {

    ForagerService service = new ForagerService(new ForagerRepositoryDouble());

    @Test
    void shouldNotAddMissingFirstName() throws DataException {
        Forager forager = new Forager();
        forager.setLastName("Green");
        forager.setState("NY");

        Result<Forager> actual = service.add(forager);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("first name"));
    }

    @Test
    void shouldNotAddMissingLastName() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Rachel");
        forager.setLastName(" ");
        forager.setState("NY");

        Result<Forager> actual = service.add(forager);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("last name"));
    }

    @Test
    void shouldNotAddMissingState() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Rachel");
        forager.setLastName("Green");

        Result<Forager> actual = service.add(forager);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("state"));
    }

    @Test
    void shouldNotAddDuplicate() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Jilly");
        forager.setLastName("Sisse");
        forager.setState("GA");

        Result<Forager> actual = service.add(forager);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotAddNull() throws DataException {
        Result<Forager> actual = service.add(null);
        assertFalse(actual.isSuccess());
    }
}