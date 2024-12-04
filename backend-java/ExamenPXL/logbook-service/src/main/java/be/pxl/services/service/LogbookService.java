package be.pxl.services.service;

import be.pxl.services.domain.Log;
import be.pxl.services.dto.LogDto;
import be.pxl.services.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogbookService implements ILogbookService {

    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(LogbookService.class);

    @Override
    public void addLog(LogDto logDto) {
        Log log = Log.builder()
                .productId(logDto.getProductId())
                .message(logDto.getMessage())
                .timestamp(LocalDateTime.now())
                .user(logDto.getUser())
                .build();
        logRepository.save(log);
        logger.info("Log added: " + log.getId());
    }

    @Override
    public List<Log> getAllLogs() {
        logger.info("Getting all logs");
        return logRepository.findAll();
    }
}
