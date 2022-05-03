package me.daniel.service;

import me.daniel.domain.ProductVO;
import me.daniel.mapper.ProductMapper;
import org.springframework.stereotype.Service;

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
}
