package be.pxl.services;

import be.pxl.services.dto.LogDto;
import be.pxl.services.dto.LogDtoMessage;
import be.pxl.services.service.LogbookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
    private final LogbookService logbookService;

    @RabbitListener(queues = "LogbookQueue")
    public void listen(LogDto logDto) {

        logger.info("Log received: " + logDto.getMessage());
        LogDto logDto2 = new LogDto(logDto.getProductId(), logDto.getMessage(), logDto.getTimestamp(), logDto.getUser());
        logbookService.addLog(logDto2);
        logger.info("Log processed: " + logDto.getMessage());
    }
}
