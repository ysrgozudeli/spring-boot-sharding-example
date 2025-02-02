package com.example.sharding_demo;

import com.example.sharding_demo.entity.Shop;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    // Find shops by name (case-sensitive)
    List<Shop> findByName(String name);

    // Find shops by organization ID
    List<Shop> findByOrganizationId(Long organizationId);

    // Find all shops created after a certain date
    List<Shop> findByCreatedAtAfter(LocalDateTime createdAt);

    // Find shops by name containing a keyword (case-insensitive)
    List<Shop> findByNameContainingIgnoreCase(String keyword);

    // Custom query: count shops by organization ID
    long countByOrganizationId(Long organizationId);
}
