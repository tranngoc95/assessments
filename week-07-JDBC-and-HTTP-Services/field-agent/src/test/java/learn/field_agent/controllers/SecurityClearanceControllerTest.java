package learn.field_agent.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityClearanceControllerTest {

    @MockBean
    SecurityClearanceRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    void shouldGetAll() throws Exception {
        List<SecurityClearance> expectedList = new ArrayList<>();
        expectedList.add(new SecurityClearance(1, "Secret"));
        expectedList.add(new SecurityClearance(2, "Top Secret"));
        expectedList.add(new SecurityClearance(3, "Confidential"));

        Mockito.when(repository.findAll()).thenReturn(expectedList);

        String expected = generateJson(expectedList);

        mvc.perform(get("/api/security-clearance"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void shouldGetById() throws Exception {
        SecurityClearance expected = new SecurityClearance(1, "Secret");

        Mockito.when(repository.findById(1)).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/security-clearance/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldAdd() throws Exception {
        SecurityClearance input = new SecurityClearance(0, "Something");
        SecurityClearance expected = new SecurityClearance(4, "Something");

        Mockito.when(repository.add(input)).thenReturn(expected);

        String inputJson = generateJson(input);
        String expectedJson = generateJson(expected);

        var request = post("/api/security-clearance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

    }

    @Test
    void shouldNotAddMissingName() throws Exception {
        SecurityClearance input = new SecurityClearance(0, "  ");

        String inputJson = generateJson(input);

        var request = post("/api/security-clearance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdate() throws Exception {
        SecurityClearance input = new SecurityClearance(1, "Something");

        Mockito.when(repository.update(input)).thenReturn(true);

        String inputJson = generateJson(input);

        var request = put("/api/security-clearance/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldSafeDelete() throws Exception {
        Mockito.when(repository.safeDeleteById(1)).thenReturn(true);

        mvc.perform(delete("/api/security-clearance/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFullDelete() throws Exception {
        Mockito.when(repository.fullDeleteById(4)).thenReturn(true);

        mvc.perform(delete("/api/security-clearance/4/full"))
                .andExpect(status().isNoContent());
    }

    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);

    }
}