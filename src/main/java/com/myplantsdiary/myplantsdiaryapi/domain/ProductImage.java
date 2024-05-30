package com.myplantsdiary.myplantsdiaryapi.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

@Embeddable
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String filename;

    @Setter
    private int ord;

}
