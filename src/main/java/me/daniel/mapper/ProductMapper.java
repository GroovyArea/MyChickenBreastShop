package me.daniel.mapper;

import me.daniel.domain.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductMapper {

    ProductVO selectProduct(int productNo);

    int selectCategoryCount(Map<String, Object> map);

    List<ProductVO> selectCategoryList(Map<String, Object> map);

    void insertProduct(ProductVO productVO);

    void updateProduct(ProductVO productVO);

    void deleteProduct(int productNo);

}
