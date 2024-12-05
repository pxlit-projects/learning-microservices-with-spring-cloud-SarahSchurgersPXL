package be.pxl.services.controller;

import be.pxl.services.LogbookServiceApplication;
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

@SpringBootTest(classes = LogbookServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class LogbookControllerTest {

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogRepository repository;


    @BeforeAll
    public static void setUp() {
        sqlContainer.start();
    }

    @Test
    public void testAddLog() throws Exception {
        LogDto logDto = LogDto.builder()
                .productId(1L)
                .message("Test message")
                .timestamp(LocalDateTime.now())
                .user("Test User")
                .build();

        mockMvc.perform(post("/logbook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logDto)))
                .andExpect(status().isCreated());

        assertEquals(1, repository.findAll().size());
    }

}