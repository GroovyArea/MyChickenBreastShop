package me.daniel.service;

import me.daniel.domain.ProductVO;
import me.daniel.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public ProductVO getProduct(int productNo) {
        return productMapper.selectProduct(productNo);
    }

    @Override
    public int getCategoryCount(Map<String, Object> map) {
        return productMapper.selectCategoryCount(map);
    }

    @Override
    public List<ProductVO> getCategoryList(Map<String, Object> map) {
        return productMapper.selectCategoryList(map);
    }

    @Override
    @Transactional
    public void addProduct(ProductVO productVO) {
        productMapper.insertProduct(productVO);
    }

    @Override
    @Transactional
    public void modifyProduct(ProductVO productVO) {
        productMapper.updateProduct(productVO);
    }

    @Override
    @Transactional
    public void removeProduct(int productNo) {
        productMapper.deleteProduct(productNo);
    }

}
