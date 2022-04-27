package me.daniel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductVO {

    private int productNo;
    private String productName;
    private String productCategory;
    private int productPrice;
    private int productStock;
    private String productDetail;
    private String productImage;
    private int product_status;
}
