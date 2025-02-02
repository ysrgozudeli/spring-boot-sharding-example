package com.example.sharding_demo;

import com.example.sharding_demo.dto.ProductDTO;
import com.example.sharding_demo.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final SearchProductService searchProductService;

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(
        @RequestParam("shopId") Long shopId,
        @RequestParam("keyword") String keyword) {

        List<ProductDTO> products = searchProductService.searchProductsByShopAndKeyword(shopId, keyword);
        return ResponseEntity.ok(products);
    }

    // TODO:
    //  this method supposed to be a @PostMapping and productDTO should come from client
    //   but it is kept like that for the sake of easy testing
    @GetMapping("/add")
    public ResponseEntity<Long> addProduct(
        @RequestParam("shopId") Long shopId) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("ABCD name");
        productDTO.setPrice(BigDecimal.valueOf(123.3));
        productDTO.setShopId(shopId);
        productDTO.setDescription(" Product TDescription");

        return ResponseEntity.ok(searchProductService.add(productDTO));
    }

}
