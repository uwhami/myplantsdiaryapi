package com.myplantsdiary.myplantsdiaryapi.service;

import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import com.myplantsdiary.myplantsdiaryapi.domain.ProductImage;
import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.PageResponseDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.ProductDTO;
import com.myplantsdiary.myplantsdiaryapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private Product dtoToEntity(ProductDTO dto) {
        Product product = Product.builder()
                .pno(dto.getPno())
                .pname(dto.getPname())
                .pdesc(dto.getPdesc())
                .price(dto.getPrice())
                .build();

        List<String> uploadFileNames = dto.getUploadFileNames();
        if(uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }

        uploadFileNames.forEach(product::addImageString);
        return product;
    }

    private ProductDTO entityToDto(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();
        if(imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

//        List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFilename()).toList();
        List<String> fileNameList = imageList.stream().map(ProductImage::getFilename).toList();
        productDTO.setUploadFileNames(fileNameList);
        return productDTO;
    }

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        //Object[] => 0 product 1 productImage
        //Object[] => 0 product 1 productImage
        //Object[] => 0 product 1 productImage

        List<ProductDTO> dtoList = result.get().map(arr -> {
            ProductDTO productDTO = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFilename();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;
        }).collect(toList());

        long totalCount = result.getTotalElements();


        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);
        productRepository.save(product);
        log.info("------------------------");
        log.info(product);
        log.info(product.getImageList());
        return product.getPno();
    }

    @Override
    public ProductDTO get(Long pno) {
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();
        return entityToDto(product);
    }

    @Override
    public void modify(ProductDTO productDTO) {
        //search
        Optional<Product> result = productRepository.findById(productDTO.getPno());
        Product product = result.orElseThrow();

        product.setPrice(productDTO.getPrice());
        product.setPname(productDTO.getPname());
        product.setPdesc(productDTO.getPdesc());
        product.setDelFlag(productDTO.isDelFlag());

        //Image
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        product.clearList();
        if(uploadFileNames != null && !uploadFileNames.isEmpty()){
            uploadFileNames.forEach(product::addImageString);
        }

        //Save
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);
    }
}
