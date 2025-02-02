package com.example.sharding_demo;

import com.example.sharding_demo.dto.ProductDTO;
import com.example.sharding_demo.dto.ProductMapper;
import com.example.sharding_demo.entity.Product;
import com.example.sharding_demo.sharding.Sharded;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class SearchProductService {

    private final ProductRepository productRepository;
    private final ShopRepository    shopRepository;
    private final ProductMapper     productMapper;

    public Long add(final ProductDTO productDTO) {

        final var shop = shopRepository.findById(productDTO.getShopId())
            .orElseThrow(() -> new RuntimeException("no such shop"));

        Product product = new Product();
        product.setCreatedAt(LocalDateTime.now());
        product.setName(productDTO.getName());
        product.setShop(shop);
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());

        productRepository.save(product);
        return product.getId();

    }

    //    @Sharded
    public List<ProductDTO> searchProductsByShopAndKeyword(Long shopId, String keyword) {

        List<Product> products = productRepository.findByShopIdAndNameContainingIgnoreCase(shopId, keyword);
        return productMapper.toDTOList(products);
    }
}
