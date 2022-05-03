package me.daniel.domain;


public class ProductVO {

    private int productNo;
    private String productName;
    private String productCategory;
    private int productPrice;
    private int productStock;
    private String productDetail;
    private String productImage;
    private int product_status;

    public ProductVO(int productNo, String productName, String productCategory, int productPrice, int productStock, String productDetail, String productImage, int product_status) {
        this.productNo = productNo;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDetail = productDetail;
        this.productImage = productImage;
        this.product_status = product_status;
    }

    public int getProductNo() {
        return productNo;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getProduct_status() {
        return product_status;
    }
}
