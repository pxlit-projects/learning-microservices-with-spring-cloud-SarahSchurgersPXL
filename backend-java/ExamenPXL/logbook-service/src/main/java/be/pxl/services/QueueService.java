package be.pxl.services;

import be.pxl.services.dto.LogDto;
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
        logbookService.addLog(logDto);
        logger.info("Log processed: " + logDto.getMessage());
    }
}
