package be.pxl.services.controller;
import be.pxl.services.domain.Log;
import be.pxl.services.dto.LogDto;
import be.pxl.services.repository.LogRepository;
import be.pxl.services.service.LogbookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class LogbookControllerMockRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }



    @MockBean
    private LogRepository repository;



    @BeforeAll
    public static void setUp() {
        sqlContainer.start();
    }

    @Test
    public void testGetAllLogs() throws Exception {
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 12, 5, 10, 0);
        Log log1 = new Log(1L, 1L, "Test message 1", fixedDateTime, "User1");
        Log log2 = new Log(2L, 2L, "Test message 2", fixedDateTime, "User2");
        List<Log> logs = Arrays.asList(log1, log2);

        when(repository.findAll()).thenReturn(logs);

        mockMvc.perform(get("/logbook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(log1.getProductId()))
                .andExpect(jsonPath("$[0].message").value(log1.getMessage()))
                .andExpect(jsonPath("$[0].timestamp").value("2023-12-05T10:00:00"))
                .andExpect(jsonPath("$[0].user").value(log1.getUser()))
                .andExpect(jsonPath("$[1].productId").value(log2.getProductId()))
                .andExpect(jsonPath("$[1].message").value(log2.getMessage()))
                .andExpect(jsonPath("$[1].timestamp").value("2023-12-05T10:00:00"))
                .andExpect(jsonPath("$[1].user").value(log2.getUser()));
    }
}
