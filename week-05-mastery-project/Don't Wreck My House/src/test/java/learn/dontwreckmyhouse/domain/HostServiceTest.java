package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.State;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService(new HostRepositoryDouble());

    @Test
    void ShouldFindByLastName() throws DataException {
        List<Host> actual = service.findByLastName("Yearnes");
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void ShouldFindByState() throws DataException {
        List<Host> actual = service.findByState(State.TX);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void ShouldFindById() throws DataException {
        assertNotNull(service.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15"));
    }

    @Test
    void shouldFindByEmail() throws DataException {
        assertNotNull(service.findByEmail("eyearnes0@sfgate.com"));
    }
}