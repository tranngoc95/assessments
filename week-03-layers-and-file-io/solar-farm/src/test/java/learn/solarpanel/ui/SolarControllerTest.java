package learn.solarpanel.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.solarpanel.data.DataAccessException;
import learn.solarpanel.data.SolarJDBCTemplateRepository;
import learn.solarpanel.domain.SolarService;
import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SolarControllerTest {

    @MockBean
    SolarJDBCTemplateRepository repository;

    @Autowired
    SolarService service;

    @Autowired
    MockMvc mvc;

    @Test
    void shouldFindBySection() throws Exception {
        List<SolarPanel> expectedList = List.of(new SolarPanel(4,"Rose",4,3,1999, MaterialType.CDTE,false));

        Mockito.when(repository.findBySection("Rose"))
                .thenReturn(expectedList);

        String expected = mapToJson(expectedList);

        mvc.perform(get("/solar-farm/Rose"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expected));
    }

    @Test
    void shouldAdd() throws Exception {
        SolarPanel panel = new SolarPanel(0,"Jasmine",4,3,2001, MaterialType.POLYSI,true);
        SolarPanel expectedPanel = new SolarPanel(6,"Jasmine",4,3,2001, MaterialType.POLYSI,true);

        Mockito.when(repository.add(panel))
                .thenReturn(expectedPanel);


        String jsonIn = mapToJson(panel);
        String expected = mapToJson(expectedPanel);

        var request = post("/solar-farm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    private String mapToJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

}