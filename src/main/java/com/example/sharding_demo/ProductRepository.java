package com.example.sharding_demo;

import com.example.sharding_demo.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShopId(Long shopId);

    List<Product> findByShopIdAndNameContainingIgnoreCase(Long shopId, String keyword);

}
