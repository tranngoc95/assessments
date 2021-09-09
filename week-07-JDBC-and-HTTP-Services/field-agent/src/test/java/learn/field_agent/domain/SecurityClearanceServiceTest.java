package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @MockBean
    SecurityClearanceRepository repository;

    @Autowired
    SecurityClearanceService service;

    // READ
    @Test
    void shouldFindAll() {
        List<SecurityClearance> expected = new ArrayList<>();
        expected.add(makeClearance());
        expected.add(new SecurityClearance(2, "Top Secret"));
        expected.add(new SecurityClearance(3, "Confidential"));
        Mockito.when(repository.findAll()).thenReturn(expected);

        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldFindById() {
        SecurityClearance expected = makeClearance();
        Mockito.when(repository.findById(1)).thenReturn(expected);
        assertEquals(expected, service.findById(1));

        Mockito.when(repository.findById(10)).thenReturn(null);
        assertNull(service.findById(10));
    }

    // CREATE
    @Test
    void shouldAdd() {
        SecurityClearance input = new SecurityClearance(0, "next level");
        SecurityClearance expected = new SecurityClearance(4, "next level");

        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<SecurityClearance> actual = service.add(input);

        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNull() {
        Result<SecurityClearance> actual = service.add(null);

        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAddSetId() {
        SecurityClearance input = new SecurityClearance(5, "next level");
        Result<SecurityClearance> actual = service.add(input);

        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotAddEmptyName() {
        SecurityClearance input = new SecurityClearance(0, "    ");
        Result<SecurityClearance> actual = service.add(input);

        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));

        input.setName(null);
        actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotAddDuplicateName() {
        SecurityClearance input = new SecurityClearance(0, "secret");
        SecurityClearance expected = makeClearance();
        Mockito.when(repository.findByName("secret")).thenReturn(expected);

        Result<SecurityClearance> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    // UPDATE
    @Test
    void shouldUpdate() {
        SecurityClearance input = makeClearance();
        input.setName("something");
        Mockito.when(repository.update(input)).thenReturn(true);

        Result<SecurityClearance> actual = service.update(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNull() {
        Result<SecurityClearance> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateNoId() {
        SecurityClearance input = makeClearance();
        input.setSecurityClearanceId(0);

        Result<SecurityClearance> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotUpdateEmptyName() {
        SecurityClearance input = makeClearance();

        input.setName("  ");
        Result<SecurityClearance> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));

        input.setName(null);
        actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotUpdateDuplicateName() {
        SecurityClearance input = new SecurityClearance(2, "secret");
        Mockito.when(repository.findByName("secret")).thenReturn(makeClearance());

        Result<SecurityClearance> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotUpdateDateMissing() {
        SecurityClearance input = new SecurityClearance(10, "something");

        Result<SecurityClearance> actual = service.update(input);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    // DELETE
    @Test
    void shouldSafeDelete() {
        Mockito.when(repository.safeDeleteById(3)).thenReturn(true);
        Result<SecurityClearance> actual = service.safeDeleteById(3);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotSafeDeleteInUseKey() {
        Mockito.when(repository.checkReferenceKey(1)).thenReturn(true);

        Result<SecurityClearance> actual = service.safeDeleteById(1);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("security clearance"));
    }

    @Test
    void shouldNotSafeDeleteMissing() {
        Result<SecurityClearance> actual = service.safeDeleteById(10);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldFullDelete() {
        Mockito.when(repository.fullDeleteById(4)).thenReturn(true);
        assertTrue(service.fullDeleteById(4));
    }

    SecurityClearance makeClearance() {
        return new SecurityClearance(1, "Secret");
    }
}