package com.myplantsdiary.myplantsdiaryapi.service;

import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.PageResponseDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.ProductDTO;
import com.myplantsdiary.myplantsdiaryapi.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResponseDTO<ProductDTO> pageResponseDTO = productService.getList(pageRequestDTO);

        log.info(pageResponseDTO.getDataList());
    }

    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 샘플")
                .pdesc("신규 추가 상품")
                .price(50000)
                .build();

        productDTO.setUploadFileNames(
                List.of(
                        UUID.randomUUID() + "_" + "TEST1.jpg",
                        UUID.randomUUID() + "_" + "TEST2.jpg"
                )
        );

        productService.register(productDTO);
    }
}
