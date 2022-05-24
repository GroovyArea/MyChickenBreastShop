package me.daniel.service;

import me.daniel.domain.DTO.ProductListDTO;
import me.daniel.domain.DTO.ProductModifyDTO;
import me.daniel.domain.VO.ProductVO;
import me.daniel.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    private final ModelMapper modelMapper;

    public ProductService(ProductMapper productMapper, ModelMapper modelMapper) {
        this.productMapper = productMapper;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public ProductListDTO findByNumber(Integer productNo) {
        return modelMapper.map(productMapper.selectNoProduct(productNo), ProductListDTO.class);
    }

    @Transactional(readOnly = true)
    public ProductListDTO findByName(String productName) {
        return modelMapper.map(productMapper.selectNameProduct(productName), ProductListDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ProductListDTO> getCategoryList(Map<String, Object> map) {
        return productMapper.selectCategoryList(map).stream()
                .map(productVO -> modelMapper.map(productVO, ProductListDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addProduct(ProductListDTO productDTO) {
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
