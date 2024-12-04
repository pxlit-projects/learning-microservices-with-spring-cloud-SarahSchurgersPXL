package be.pxl.services.service;

import be.pxl.services.domain.Log;
import be.pxl.services.dto.LogDto;

import java.util.List;

public interface ILogbookService {
    void addLog(LogDto logDto);
    List<Log> getAllLogs();
}
