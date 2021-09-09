package learn.field_agent.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SingleAliasAgent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AliasControllerTest {
    @MockBean
    AliasRepository repository;

    @MockBean
    AgentRepository agentRepository;

    @Autowired
    MockMvc mvc;

    @Test
    void shouldFindAll() throws Exception {
        List<Alias> expected = new ArrayList<>();
        expected.add(makeExistingAlias());
        expected.add(new Alias(2, "Red", "Persona 1", 5));
        expected.add(new Alias(3, "Red", "Persona 2", 3));
        expected.add(new Alias(4, "Donna", null, 1));

        Mockito.when(repository.findAll()).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/alias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldFindById() throws Exception {
        Alias alias = makeExistingAlias();

        Mockito.when(repository.findById(1)).thenReturn(alias);

        String expectedJson = generateJson(alias);

        mvc.perform(get("/api/alias/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldFindAliasAgentById() throws Exception{
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

        String expectedJson = generateJson(aliasAgent);

        mvc.perform(get("/api/alias/1/agent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldAdd() throws Exception {
        Alias alias = makeNewAlias();
        Alias expected = makeNewAlias();
        expected.setAliasId(5);

        Mockito.when(repository.add(alias)).thenReturn(expected);

        String inputJson = generateJson(alias);
        String expectedJson = generateJson(expected);

        var request = post("/api/alias/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldUpdate() throws Exception {
        Alias alias = makeExistingAlias();

        Mockito.when(repository.update(alias)).thenReturn(true);

        String aliasJson = generateJson(alias);

        var request = put("/api/alias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aliasJson);

        mvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldNotUpdateConflict() throws Exception {
        Alias alias = makeExistingAlias();

        String aliasJson = generateJson(alias);

        var request = put("/api/alias/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aliasJson);

        mvc.perform(request).andExpect(status().isConflict());
    }

    @Test
    void shouldDelete() throws Exception {
        Mockito.when(repository.deleteById(1)).thenReturn(true);
        mvc.perform(delete("/api/alias/1"))
                .andExpect(status().isNoContent());
    }

    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
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