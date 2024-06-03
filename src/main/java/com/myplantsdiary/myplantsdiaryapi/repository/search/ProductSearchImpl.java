package com.myplantsdiary.myplantsdiaryapi.repository.search;

import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import com.myplantsdiary.myplantsdiaryapi.domain.QProduct;
import com.myplantsdiary.myplantsdiaryapi.domain.QProductImage;
import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.PageResponseDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.ProductDTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    //Querydsl을 사용하여 가지고 오기
    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO){
        log.info("----------------------------searchList--------------------------------");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("pno").descending());

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.imageList, productImage);    //entity 가 아니기 때문에 product.imageList 로 가지구 와야 한다.

        query.where(productImage.ord.eq(0));

        getQuerydsl().applyPagination(pageable, query);

//        List<Product> productList = query.fetch();
        List<Tuple> productList = query.select(product, productImage).fetch();
        long count = query.fetchCount();

        log.info("=======================================");
        log.info("Total products found: " + count);
        log.info(productList);

        return null;
    }

}
