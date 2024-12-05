package be.pxl.services.repository;

import be.pxl.services.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Object> findAllByName(String testProduct);

    int countAllById(long l);
}