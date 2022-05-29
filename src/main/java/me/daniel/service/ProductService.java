package me.daniel.service;

import me.daniel.domain.DTO.CartItemDTO;
import me.daniel.domain.DTO.ProductListDTO;
import me.daniel.domain.DTO.ProductModifyDTO;
import me.daniel.domain.VO.ProductVO;
import me.daniel.exceptions.InvalidPayAmountException;
import me.daniel.exceptions.InvalidProductException;
import me.daniel.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 상품 서비스 <br>
 * 상품 조회, 추가, 수정, 삭제 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.2, 2022.05.28 유효성 메서드 추가
 * </pre>
 *
 * @author 김남영
 * @version 1.2
 */
@Service
public class ProductService {

    private static final String INVALID_PRODUCT = "존재하지 않는 상품 번호입니다. 다시 확인 바랍니다.";
    private static final String INVALID_PAY_AMOUNT = "상품 총 가격이 잘못 되었습니다.";

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

    public void validateProduct(int productNo) throws InvalidProductException {
        if (productMapper.selectNoProduct(productNo) == null) {
            throw new InvalidProductException(INVALID_PRODUCT);
        }
    }

    public void validatePayAmount(CartItemDTO cartItemDTO) throws InvalidPayAmountException {
        if (productMapper.selectNoProduct(cartItemDTO.getProductNo()).getProductPrice() * cartItemDTO.getProductStock() != cartItemDTO.getProductPrice()) {
            throw new InvalidPayAmountException(INVALID_PAY_AMOUNT);
        }
    }

}
