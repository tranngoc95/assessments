package learn.field_agent.data;

import learn.field_agent.models.Alias;
import learn.field_agent.models.SingleAliasAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

    @Autowired
    AliasRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Alias> aliases = repository.findAll();
        assertNotNull(aliases);
        assertTrue(aliases.size()>=3);
    }

    @Test
    void shouldFindByName() {
        List<Alias> aliases = repository.findByName("Red");
        assertNotNull(aliases);
        assertTrue(aliases.size()>=1);
    }

    @Test
    void shouldFindById() {
        Alias actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals("Kitty", actual.getName());
    }

    @Test
    void shouldAdd() {
        Alias input = new Alias(0, "Hyde", null, 2);
        Alias actual = repository.add(input);
        assertNotNull(actual);
        assertEquals(5, actual.getAliasId());
    }

    @Test
    void shouldUpdate() {
        Alias input = new Alias(2, "Hyde", null, 2);
        assertTrue(repository.update(input));
    }

    @Test
    void shouldNotUpdateMissing() {
        Alias input = new Alias(12, "Hyde", null, 2);
        assertFalse(repository.update(input));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteById(10));
    }
}