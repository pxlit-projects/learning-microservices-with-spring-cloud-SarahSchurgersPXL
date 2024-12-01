package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ShoppingcartApplication
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ShoppingCartService
{
    public static void main( String[] args )
    {
        SpringApplication.run(ShoppingCartService.class, args);
    }
}
