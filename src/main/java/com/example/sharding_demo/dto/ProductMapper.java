package com.example.sharding_demo.dto;

import com.example.sharding_demo.entity.Product;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {

        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getShop().getId()
        );
    }

    public List<ProductDTO> toDTOList(List<Product> products) {

        return products.stream()
            .map(this::toDTO)
            .toList();
    }
}
