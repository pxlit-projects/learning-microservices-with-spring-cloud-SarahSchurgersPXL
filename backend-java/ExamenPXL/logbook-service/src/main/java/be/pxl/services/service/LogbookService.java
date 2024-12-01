package be.pxl.services.service;

import be.pxl.services.domain.Log;
import be.pxl.services.dto.LogDto;
import be.pxl.services.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogbookService implements ILogbookService {

    private final LogRepository logRepository;

    @Override
    public void addLog(LogDto logDto) {
        Log log = Log.builder()
                .productId(logDto.getProductId())
                .message(logDto.getMessage())
                .timestamp(LocalDateTime.now())
                .user(logDto.getUser())
                .build();
        logRepository.save(log);
    }
}
