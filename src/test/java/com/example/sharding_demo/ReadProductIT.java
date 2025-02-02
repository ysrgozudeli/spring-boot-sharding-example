package com.example.sharding_demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReadProductIT extends BaseIT {

    @Test
    @Sql({"/sql/init_products.sql"})
    void valid_authorization_valid_shopId_Success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/products/search?shopId={shopId}&keyword=o", 1)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }
}