package be.pxl.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="shoppingcartitem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    @Transient
    private Product product;
    private int quantity;

    @ManyToOne
    @JsonIgnore
    private ShoppingCart shoppingCart;
}
