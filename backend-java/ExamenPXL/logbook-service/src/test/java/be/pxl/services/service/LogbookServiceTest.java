package be.pxl.services.service;

import be.pxl.services.domain.Log;
import be.pxl.services.dto.LogDto;
import be.pxl.services.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LogbookServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogbookService logbookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testAddLog() {
        LogDto logDto = LogDto.builder()
                .productId(1L)
                .message("Test message")
                .timestamp(LocalDateTime.now())
                .user("Test User")
                .build();


        logbookService.addLog(logDto);

        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    public void testGetAllLogs() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 12, 5, 10, 0);
        Log log1 = new Log(1L, 1L, "Test message 1", fixedDateTime, "User1");
        Log log2 = new Log(2L, 2L, "Test message 2", fixedDateTime, "User2");
        List<Log> logs = Arrays.asList(log1, log2);

        when(logRepository.findAll()).thenReturn(logs);

        List<Log> result = logbookService.getAllLogs();

        assertEquals(2, result.size());
        assertEquals(log1, result.get(0));
        assertEquals(log2, result.get(1));
    }
}