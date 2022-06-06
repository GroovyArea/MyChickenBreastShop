package me.daniel.domain.DTO.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductModifyDTO {

    private Integer productNo;
    private String productName;
    private String productCategory;
    private Integer productPrice;
    private Integer productStock;
    private String productDetail;
    private String productImage;
    private Integer productStatus;
}
