package be.pxl.services.client;

import be.pxl.services.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productcatalog-service")
public interface ProductClient {
    @GetMapping("product/{id}")
    Product getProductById(@PathVariable Long id);
}
