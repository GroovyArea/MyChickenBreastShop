package com.daniel.domain.DTO.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Integer productNo;
    private String productName;
    private String productCategory;
    private Integer productPrice;
    private Integer productStock;
    private String productDetail;
    private String productImage;
    private Integer productStatus;
}
