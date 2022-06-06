package me.daniel.domain.DTO.product;

public class ProductListDTO {

    private Integer productNo;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productImage;

    public ProductListDTO() {

    }

    public ProductListDTO(Integer productNo, String productName, Integer productPrice, Integer productStock, String productImage) {
        this.productNo = productNo;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productImage = productImage;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public String getProductImage() {
        return productImage;
    }

}
