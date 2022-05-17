package me.daniel.service;

import me.daniel.domain.DTO.ProductDTO;
import me.daniel.domain.DTO.ProductModifyDTO;
import me.daniel.domain.VO.ProductVO;
import me.daniel.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    private final ModelMapper modelMapper;

    public ProductService(ProductMapper productMapper, ModelMapper modelMapper) {
        this.productMapper = productMapper;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public ProductDTO findByNumber(Integer productNo) {
        return modelMapper.map(productMapper.selectNoProduct(productNo), ProductDTO.class);
    }

    @Transactional(readOnly = true)
    public ProductDTO findByName(String productName) {
        return modelMapper.map(productMapper.selectNameProduct(productName), ProductDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ProductVO> getCategoryList(Map<String, Object> map) {
        return productMapper.selectCategoryList(map);
    }

    @Transactional
    public void addProduct(ProductDTO productDTO) {
        productMapper.insertProduct(modelMapper.map(productDTO, ProductVO.class));
    }

    @Transactional
    public void modifyProduct(ProductModifyDTO productModifyDTO) {
        productMapper.updateProduct(modelMapper.map(productModifyDTO, ProductVO.class));
    }

    @Transactional
    public void removeProduct(Map<String, Object> map) {
        productMapper.deleteProduct(map);
    }

}
