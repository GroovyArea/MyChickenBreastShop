package com.daniel.domain.DTO.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductListDTO {

    private Integer productNo;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productImage;
}
