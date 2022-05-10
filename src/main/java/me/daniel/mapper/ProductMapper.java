package me.daniel.mapper;

import me.daniel.domain.DTO.ProductDTO;
import me.daniel.domain.VO.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductMapper {

    ProductVO selectNoProduct(Integer productNo);
    ProductVO selectNameProduct(String productName);

    List<ProductVO> selectCategoryList(Map<String, Object> map);

    void insertProduct(ProductVO productVO);

    void updateProduct(ProductVO productVO);

    void deleteProduct(int productNo);

}
