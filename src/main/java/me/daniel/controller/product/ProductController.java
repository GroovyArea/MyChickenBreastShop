package me.daniel.controller.product;

import me.daniel.Enum.ChickenStatus;
import me.daniel.domain.ProductVO;
import me.daniel.service.ProductService;
import me.daniel.utility.Pager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 상품 관련 Controller
 * 상품 조회, 추가, 수정, 삭제 요청
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * page 당 게시글 수를 찾기 위한 map 객체
     */
    private Map<String, Object> countMap = new HashMap<>();
    /**
     * page 당 게시글 리스트를 반환 받기 위한 map 객체
     */
    private Map<String, Object> pagerMap = new HashMap<>();

    /**
     * 게시글 데이터를 반환하기 위한 map 객체
     */
    private Map<String, Object> returnMap = new HashMap<>();

    /**
     * 단일 상품 디테일 객체를 반환하는 메서드
     *
     * @param productNo
     * @return productVO
     */
    @GetMapping("/detail/{productNo}")
    public ProductVO detailPage(@PathVariable int productNo) {
        return productService.getProduct(productNo);
    }

    /**
     * page 당 상품 리스트 반환하는 메서드
     *
     * @param productCategoryNo
     * @param pageNum
     * @return returnMap
     */
    @GetMapping("/list/{productCategoryNo}")
    public Map<String, Object> listPage(@PathVariable(value = "productCategoryNo") int productCategoryNo,
                                        @RequestParam(defaultValue = "1") int pageNum
    ) {

        countMap.put("productCategory", productCategoryNo);
        countMap.put("productStatus", ChickenStatus.SALE.getValue());

        int totalProduct = productService.getCategoryCount(countMap);
        int productSize = 5;
        int blockSize = 8;
        //int number = totalProduct - (pageNum-1) * productSize;

        // 페이징 객체
        Pager pager = new Pager(pageNum, totalProduct, productSize, blockSize);

        pagerMap.put("productCategory", productCategoryNo);
        pagerMap.put("startRow", pager.getStartRow() - 1);
        pagerMap.put("rowCount", blockSize);


        returnMap.put("productList", productService.getCategoryList(pagerMap));
        returnMap.put("pager", pager);

        return returnMap;
    }


    // 상품 추가, 수정, 삭제 핸들러

}
