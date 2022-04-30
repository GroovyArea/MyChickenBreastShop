package me.daniel.service;

import lombok.RequiredArgsConstructor;
import me.daniel.domain.ProductVO;
import me.daniel.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

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
