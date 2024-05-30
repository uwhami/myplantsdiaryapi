package com.myplantsdiary.myplantsdiaryapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name="tbl_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void addImage(ProductImage image) {
        image.setOrd(imageList.size());
        imageList.add(image);

    }

    public void addImageString(String fileName){
        ProductImage productImage = ProductImage.builder()
                .filename(fileName)
                .build();

        addImage(productImage);
    }

    public void clearList(){
        this.imageList.clear();
    }

}
