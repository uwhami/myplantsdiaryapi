package com.myplantsdiary.myplantsdiaryapi.repository;

import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert(){
        Product product = Product.builder().pname("test ").pdesc("test desc").price(1000).build();
        product.addImageString(UUID.randomUUID() + "_" + "IMAGE1.jpg");
        product.addImageString(UUID.randomUUID() + "_" + "IMAGE2.jpg");
        productRepository.save(product);
    }
}
