package be.pxl.services.service;

import be.pxl.services.dto.LogDto;

public interface ILogbookService {
    void addLog(LogDto logDto);
}
