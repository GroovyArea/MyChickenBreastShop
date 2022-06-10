package com.daniel.domain.DTO.cart;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class CartItemDTO {

    private final Integer productNo;
    private String productName;
    private Integer productStock;
    private Integer productPrice;

    @ConstructorProperties({"productNo", "productName","productStock", "productPrice"})

    public CartItemDTO(Integer productNo, String productName, Integer productStock, Integer productPrice) {
        this.productNo = productNo;
        this.productName = productName;
        this.productStock = productStock;
        this.productPrice = productPrice;
    }
}
