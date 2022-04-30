package me.daniel.mapper;

import me.daniel.domain.ProductVO;
import me.daniel.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductMapper {

    // 단일 상품 조회
    ProductVO selectProduct(int productNo);

    // 카테고리별 상품 개수 조회
    int selectCategoryCount(Map<String, Object> map);

    // 카테고리별 상품 리스트 조회
    List<ProductVO> selectCategoryList(Map<String, Object> map);
}
