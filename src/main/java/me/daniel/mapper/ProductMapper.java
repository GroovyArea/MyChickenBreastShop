package me.daniel.mapper;

import me.daniel.domain.VO.ProductVO;
import me.daniel.utility.Pager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductMapper {

    ProductVO selectNoProduct(Integer productNo);

    ProductVO selectNameProduct(String productName);

    List<ProductVO> selectCategoryList(@Param("pager") Pager pager, @Param("productCategory") int productCategoryNo);

    void insertProduct(ProductVO productVO);

    void updateProduct(ProductVO productVO);

    void deleteProduct(Map<String, Object> map);

}
