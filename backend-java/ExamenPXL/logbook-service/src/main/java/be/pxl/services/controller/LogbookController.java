package be.pxl.services.controller;

import be.pxl.services.dto.LogDto;
import be.pxl.services.service.ILogbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logbook")
@RequiredArgsConstructor
public class LogbookController {

    private final ILogbookService logbookService;

    @PostMapping
    public ResponseEntity<Void> addLog(@RequestBody LogDto logDto) {
        logbookService.addLog(logDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




}
