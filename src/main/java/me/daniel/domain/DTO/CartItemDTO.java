package me.daniel.domain.DTO;

public class CartItemDTO {

    private final Integer productNo;
    private Integer productStock;
    private final String productPrice;

    public CartItemDTO(Integer productNo, Integer productStock, String productPrice) {
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

}
