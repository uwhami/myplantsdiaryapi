package com.myplantsdiary.myplantsdiaryapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino;

    private int quantity;

    private String pname;

    private int price;

    private String imageFile;

    public CartItemListDTO(Long cino, int quantity, String pname, int price, String imageFile) {
        this.cino = cino;
        this.quantity = quantity;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}
