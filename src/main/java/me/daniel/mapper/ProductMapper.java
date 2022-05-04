package me.daniel.mapper;

import me.daniel.domain.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductMapper {

    /**
     * 단일 상품 조회
     *
     * @param productNo
     * @return ProductVO
     */
    ProductVO selectProduct(int productNo);

    /**
     * 카테고리별 상품 개수 조회
     *
     * @param map
     * @return count
     */
    int selectCategoryCount(Map<String, Object> map);

    /**
     * 카테고리별 상품 리스트 조회
     *
     * @param map
     * @return List
     */
    List<ProductVO> selectCategoryList(Map<String, Object> map);

    /**
     * 상품 추가
     *
     * @param productVO
     */
    void insertProduct(ProductVO productVO);

    /**
     * 상품 수정
     *
     * @param productVO
     */
    void updateProduct(ProductVO productVO);

    /**
     * 상품 삭제
     *
     * @param productNo
     */
    void deleteProduct(int productNo);

}
