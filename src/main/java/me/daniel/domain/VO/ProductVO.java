package me.daniel.domain.VO;


public class ProductVO {

    private final Integer productNo;
    private final String productName;
    private final String productCategory;
    private final Integer productPrice;
    private final Integer productStock;
    private final String productDetail;
    private final String productImage;
    private final Integer productStatus;

    public ProductVO(Integer productNo, String productName, String productCategory, Integer productPrice, Integer productStock, String productDetail, String productImage, Integer productStatus) {
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

    public Integer productStatus() {
        return productStatus;
    }
}
