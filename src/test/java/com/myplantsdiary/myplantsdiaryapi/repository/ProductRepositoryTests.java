package com.myplantsdiary.myplantsdiaryapi.repository;

import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert(){

        for(int i=0; i<15 ;i++){
            Product product = Product.builder().pname("test ").pdesc("test desc").price(1000).build();
            product.addImageString(UUID.randomUUID() + "_" + "IMAGE1_" + i + ".jpg");
            product.addImageString(UUID.randomUUID() + "_" + "IMAGE2_" + i + ".jpg");
            productRepository.save(product);
        }

    }

    @Transactional
    @Test
    public void testRead1(){
        Long pno = 1L;
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();

        log.info(product);
        log.info(product.getPname());   //쿼리를 두 번 날리기 때문에 transactional 안하면 에러 발생.
    }

    @Test
    public void testRead2(){
        Long pno = 1L;
        Optional<Product> result = productRepository.selectOne(pno);    //조인해서 쿼리를 한번만 날림.
        Product product = result.orElseThrow();

        log.info(product);
        log.info(product.getPname());
    }

    @Commit
    @Transactional
    @Test
    public void testDelete(){
        Long pno = 2L;
        productRepository.updateToDelete(true, pno);
    }

    @Test
    public void testUpdate(){
        Product product = productRepository.selectOne(1L).get();

        product.setPrice(3000);
        //jpa 에서 collection을 사용 시에 기존에 물고 있는 collection을 사용해야한다.
        product.clearList();

        product.addImageString(UUID.randomUUID() + "_" + "PIMAGE1.jpg");
        product.addImageString(UUID.randomUUID() + "_" + "PIMAGE2.jpg");
        product.addImageString(UUID.randomUUID() + "_" + "PIMAGE3.jpg");

        productRepository.save(product);
    }

    @Test
    public void testList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        productRepository.searchList(pageRequestDTO);
    }

}
