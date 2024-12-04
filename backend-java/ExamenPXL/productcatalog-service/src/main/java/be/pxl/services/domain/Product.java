package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @ElementCollection
    @CollectionTable(name = "product_label", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "label")
    private List<String> labels;

}
