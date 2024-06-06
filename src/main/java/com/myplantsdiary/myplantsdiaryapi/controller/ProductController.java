package com.myplantsdiary.myplantsdiaryapi.controller;

import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.PageResponseDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.ProductDTO;
import com.myplantsdiary.myplantsdiaryapi.service.ProductService;
import com.myplantsdiary.myplantsdiaryapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;
    private final ServerProperties serverProperties;

//    @PostMapping("/")
//    public Map<String, String> register(ProductDTO productDTO) {
//
//        log.info("========== register: " + productDTO);
//
//        List<MultipartFile> files = productDTO.getFiles();
//
//        List<String> uploadedFileNames = fileUtil.saveFiles(files);
//
//        productDTO.setUploadFileNames(uploadedFileNames);
//
//        log.info(uploadedFileNames);
//
//
//        return Map.of("RESULT","SUCCESS");
//    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO requestDTO){

        return productService.getList(requestDTO);

    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames);

        log.info("========== uploadFileNames: " + uploadFileNames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
     }

     @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno){
        return productService.get(pno);
     }

}
