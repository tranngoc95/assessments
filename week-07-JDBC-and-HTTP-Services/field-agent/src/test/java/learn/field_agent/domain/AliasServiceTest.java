package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SingleAliasAgent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasServiceTest {

    @MockBean
    AliasRepository repository;

    @MockBean
    AgentRepository agentRepository;

    @Autowired
    AliasService service;

    // READ
    @Test
    void shouldFindAll() {
        List<Alias> expected = new ArrayList<>();
        expected.add(makeExistingAlias());
        expected.add(new Alias(2, "Red", "Persona 1", 5));
        expected.add(new Alias(3, "Red", "Persona 2", 3));
        expected.add(new Alias(4, "Donna", null, 1));

        Mockito.when(repository.findAll()).thenReturn(expected);

        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldFindById() {
        Alias expected = makeExistingAlias();
        Mockito.when(repository.findById(1)).thenReturn(expected);
        assertEquals(expected, service.findById(1));

        Mockito.when(repository.findById(10)).thenReturn(null);
        assertNull(service.findById(10));
    }

    @Test
    void shouldFindAliasAgentById() {
        SingleAliasAgent aliasAgent = new SingleAliasAgent();
        aliasAgent.setAlias(makeExistingAlias());
        Alias other = new Alias(4, "Donna", null, 1);

        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setFirstName("Hazel");
        agent.setMiddleName("C");
        agent.setLastName("Sauven");
        agent.setHeightInInches(76);
        agent.setAliases(List.of(makeExistingAlias(), other));

        aliasAgent.setAgent(agent);

        Mockito.when(repository.findById(1)).thenReturn(makeExistingAlias());
        Mockito.when(agentRepository.findById(1)).thenReturn(agent);

        SingleAliasAgent actual = service.findAliasAgentById(1);
        assertEquals(aliasAgent, actual);
    }

    @Test
    void shouldNotFindAliasAgentMissingById() {
        Mockito.when(repository.findById(1)).thenReturn(null);
        assertNull(service.findAliasAgentById(1));
    }

    // CREATE
    @Test
    void shouldAdd() {
        Alias input = makeNewAlias();
        Alias expected = makeNewAlias();
        expected.setAliasId(5);


        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<Alias> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());

        input.setPersona("this");
        Alias found = makeNewAlias();
        found.setAliasId(2);
        found.setPersona("that");

        Mockito.when(repository.findByName(input.getName())).thenReturn(List.of(found));
        actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotAddPresetID() {
        Alias input = makeNewAlias();
        input.setAliasId(7);

        Result<Alias> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotAddNull() {
        Result<Alias> actual = service.add(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAddEmptyName() {
        Alias input = makeNewAlias();
        input.setName("");

        Result<Alias> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));

        input.setName(null);

        actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotAddDuplicateNameNullPersona() {
        Alias input = makeNewAlias();
        Alias found = makeNewAlias();
        found.setAliasId(2);

        Mockito.when(repository.findByName(input.getName())).thenReturn(List.of(found));

        Result<Alias> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("persona"));
    }

    @Test
    void shouldNotAddDuplicateNameAndPersona() {
        Alias input = makeNewAlias();
        input.setPersona("this persona");
        Alias found = makeNewAlias();
        found.setAliasId(2);
        found.setPersona("this persona");

        Mockito.when(repository.findByName(input.getName())).thenReturn(List.of(found));

        Result<Alias> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    // UPDATE
    @Test
    void shouldUpdate() {
        Alias alias = makeExistingAlias();
        alias.setPersona("new persona");

        Mockito.when(repository.update(alias)).thenReturn(true);

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNoId() {
        Alias alias = makeExistingAlias();
        alias.setAliasId(0);

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotUpdateNull() {
        Result<Alias> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateEmptyName() {
        Alias alias = makeExistingAlias();
        alias.setName(" ");

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));

        alias.setName(null);

        actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotUpdateDuplicateNameNullPersona() {
        Alias alias = makeExistingAlias();
        Alias found = makeExistingAlias();
        found.setAliasId(2);

        Mockito.when(repository.findByName(alias.getName())).thenReturn(List.of(found));

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("persona"));
    }

    @Test
    void shouldNotUpdateDuplicateNameAndPersona() {
        Alias alias = makeExistingAlias();
        alias.setPersona("the same");
        Alias found = makeExistingAlias();
        found.setAliasId(2);
        found.setPersona("the same");

        Mockito.when(repository.findByName(alias.getName())).thenReturn(List.of(found));

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name and persona"));
    }

    @Test
    void shouldNotUpdateMissing() {
        Alias alias = makeExistingAlias();
        alias.setAliasId(10);

        Mockito.when(repository.update(alias)).thenReturn(false);

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    // DELETE
    @Test
    void shouldDelete() {
        Mockito.when(repository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissing() {
        Mockito.when(repository.deleteById(10)).thenReturn(false);
        assertFalse(service.deleteById(10));
    }

    // Helper methods
    private Alias makeExistingAlias(){
        Alias alias = new Alias();
        alias.setAliasId(1);
        alias.setName("Kitty");
        alias.setAgentId(1);
        return alias;
    };

    private Alias makeNewAlias(){
        Alias alias = new Alias();
        alias.setName("Kimmy");
        alias.setAgentId(6);
        return alias;
    };
}