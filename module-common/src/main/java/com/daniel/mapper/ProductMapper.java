package com.daniel.mapper;

import com.daniel.domain.vo.ProductVO;
import com.daniel.utility.Pager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductMapper {

    ProductVO selectNoProduct(int productNo);

    ProductVO selectNameProduct(String productName);

    List<ProductVO> selectCategoryList(String searchKeyword, String searchValue, @Param("pager") Pager pager, @Param("productCategory") int productCategoryNo);

    int selectStockOfProduct(String productName);

    void insertProduct(ProductVO productVO);

    void updateProduct(ProductVO productVO);

    void updateStockOfProduct(Map<String, Object> map);

    void deleteProduct(Map<String, Object> map);

}
