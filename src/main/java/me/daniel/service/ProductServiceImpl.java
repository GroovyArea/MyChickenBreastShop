package me.daniel.service;

import lombok.RequiredArgsConstructor;
import me.daniel.domain.ProductVO;
import me.daniel.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public ProductVO getProduct(int productNo) {
        return productMapper.selectProduct(productNo);
    }
}
