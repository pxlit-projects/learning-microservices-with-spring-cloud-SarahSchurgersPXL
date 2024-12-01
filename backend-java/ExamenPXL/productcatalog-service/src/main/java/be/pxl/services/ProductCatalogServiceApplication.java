package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ProductCatalogApplication
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ProductCatalogServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProductCatalogServiceApplication.class, args);
    }
}
