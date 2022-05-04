package me.daniel.service;

import me.daniel.domain.ProductVO;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ProductVO getProduct(int productNo);

    int getCategoryCount(Map<String, Object> map);

    List<ProductVO> getCategoryList(Map<String, Object> map);

    void addProduct(ProductVO productVO);

    void modifyProduct(ProductVO productVO);

    void removeProduct(int productNo);

}
