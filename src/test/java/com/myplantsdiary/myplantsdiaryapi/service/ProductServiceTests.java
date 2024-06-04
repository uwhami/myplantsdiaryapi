package com.myplantsdiary.myplantsdiaryapi.service;

import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.PageResponseDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResponseDTO<ProductDTO> pageResponseDTO = productService.getList(pageRequestDTO);

        log.info(pageResponseDTO.getDataList());
    }
}
