package be.pxl.services.client;


import be.pxl.services.dto.LogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "logbook-service")
public interface LogClient {
    @PostMapping("/logbook")
    ResponseEntity<Void> addLog(@RequestBody LogDto logDto);

}
