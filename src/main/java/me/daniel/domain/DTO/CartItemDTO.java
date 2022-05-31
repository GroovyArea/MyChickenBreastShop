package me.daniel.domain.DTO;

import java.beans.ConstructorProperties;

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

    public Integer getProductNo() {
        return productNo;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }
}
