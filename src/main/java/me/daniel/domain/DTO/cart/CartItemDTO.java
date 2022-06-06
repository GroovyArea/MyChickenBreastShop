package me.daniel.domain.DTO.cart;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class CartItemDTO {

    private final Integer productNo;
    private Integer productStock;
    private Integer productPrice;

    @ConstructorProperties({"productNo", "productStock", "productPrice"})
    public CartItemDTO(Integer productNo, Integer productStock, Integer productPrice) {
        this.productNo = productNo;
        this.productStock = productStock;
        this.productPrice = productPrice;
    }
}
