package be.pxl.services.controller;

import be.pxl.services.domain.Log;
import be.pxl.services.dto.LogDto;
import be.pxl.services.service.ILogbookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logbook")
@RequiredArgsConstructor
public class LogbookController {

    private final ILogbookService logbookService;
    private static final Logger logger = LoggerFactory.getLogger(LogbookController.class);

    @PostMapping
    public ResponseEntity<Void> addLog(@RequestBody @Valid LogDto logDto) {
        logbookService.addLog(logDto);
        logger.info("received requests to add log");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Log>> getAllLogs() {
        List<Log> logs = logbookService.getAllLogs();
        logger.info("received requests to get all logs");
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }




}
