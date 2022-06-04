package me.daniel.domain.DTO.product;

public class ProductModifyDTO {

    private Integer productNo;
    private String productName;
    private String productCategory;
    private Integer productPrice;
    private Integer productStock;
    private String productDetail;
    private String productImage;
    private Integer productStatus;

    public ProductModifyDTO(Integer productNo, String productName, String productCategory, Integer productPrice, Integer productStock, String productDetail, String productImage, Integer productStatus) {
        this.productNo = productNo;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDetail = productDetail;
        this.productImage = productImage;
        this.productStatus = productStatus;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public String getProductImage() {
        return productImage;
    }

    public Integer getProductStatus() {
        return productStatus;
    }
}
