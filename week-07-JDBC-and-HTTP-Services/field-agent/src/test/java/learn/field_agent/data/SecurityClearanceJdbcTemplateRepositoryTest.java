package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(10);
        assertNull(actual);
    }

    @Test
    void shouldFindAll() {
        List<SecurityClearance> actual = repository.findAll();
        assertTrue(actual.size()>=2);
    }

    @Test
    void shouldFindByName() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");

        SecurityClearance actual = repository.findByName("Secret");
        assertEquals(secret, actual);

        actual = repository.findByName("Something");
        assertNull(actual);
    }

    @Test
    void shouldAdd() {
        SecurityClearance actual = repository.add(new SecurityClearance(0, "top top secret"));
        assertNotNull(actual);
        assertEquals(5, actual.getSecurityClearanceId());
    }

    @Test
    void shouldNotUpdateMissing() {
        assertFalse(repository.update(new SecurityClearance(10, "next level")));
    }

    @Test
    void shouldUpdateExisting() {
        assertTrue(repository.update(new SecurityClearance(2, "top tier secret")));
        assertEquals("top tier secret", repository.findById(2).getName());
    }

    @Test
    void shouldNotDeleteMissingId() {
        assertFalse(repository.safeDeleteById(10));
    }

    @Test
    void shouldSafeDeleteExistingId() {
        assertTrue(repository.safeDeleteById(3));
        assertNull(repository.findById(3));
    }

    @Test
    void shouldFullDeleteExistingId() {
        assertTrue(repository.fullDeleteById(4));
        assertNull(repository.findById(4));
    }

    @Test
    void shouldBeAbleToCheckReferenceKey() {
        assertFalse(repository.checkReferenceKey(2));
        assertTrue(repository.checkReferenceKey(1));
    }
}