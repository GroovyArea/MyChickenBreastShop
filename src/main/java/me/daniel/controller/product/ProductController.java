package me.daniel.controller.product;

import me.daniel.Enum.ChickenStatus;
import me.daniel.domain.ProductVO;
import me.daniel.service.ProductService;
import me.daniel.utility.Pager;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 상품 관련 Controller
 * 상품 조회, 추가, 수정, 삭제 요청
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
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
     * @param productNo 상품 번호
     * @return ResponseEntity 상품 정보
     */
    @GetMapping("/{productNo}")
    public ResponseEntity productDetail(@PathVariable int productNo) {
        return new ResponseEntity(productService.getProduct(productNo), HttpStatus.OK);
    }

    /**
     * page 당 상품 리스트 반환하는 메서드
     *
     * @param productCategoryNo 상품 카테고리 번호
     * @param pageNum 페이지 번호
     * @return ResponseEntity 페이지 정보, 상품 카테고리 리스트
     */
    @GetMapping("/list/{productCategoryNo}")
    public ResponseEntity productList(@PathVariable(value = "productCategoryNo") int productCategoryNo,
                                        @RequestParam(defaultValue = "1") int pageNum
    ) {
        countMap.put("productCategory", productCategoryNo);
        countMap.put("productStatus", ChickenStatus.SALE.getValue());

        int totalProduct = productService.getCategoryCount(countMap);
        int productSize = 5;
        int blockSize = 8;
        //int number = totalProduct - (pageNum-1) * productSize;

        Pager pager = new Pager(pageNum, totalProduct, productSize, blockSize);

        pagerMap.put("productCategory", productCategoryNo);
        pagerMap.put("startRow", pager.getStartRow() - 1);
        pagerMap.put("rowCount", blockSize);

        returnMap.put("productList", productService.getCategoryList(pagerMap));

        return ResponseEntity.ok().body(productService.getCategoryList(pagerMap));
    }

    /**
     * 상품 추가
     *
     * @param productVO 추가할 상품 정보
     * @return ResponseEntity 추가된 상품 정보
     */
    @PostMapping("add")
    public ResponseEntity addAction(@ModelAttribute ProductVO productVO) {
        productService.addProduct(productVO);
        return ResponseEntity.ok().body(productService.getProduct(productVO.getProductNo()));
    }

    /**
     * 상품 수정
     *
     * @param productVO 수정할 상품 정보
     * @return ResponseEntity 수정된 상품 정보
     */
    @PutMapping("modify")
    public ResponseEntity modifyAction(@ModelAttribute ProductVO productVO) {
        productService.modifyProduct(productVO);
        return ResponseEntity.ok().body(productService.getProduct(productVO.getProductNo()));
    }

    /**
     * 상품 삭제 (status 0으로 값 변경)
     *
     * @param productNo 삭제할 상품 번호
     * @return ResponseEntity Success
     */
    @DeleteMapping("delete/{productNo}")
    public ResponseEntity deleteAction(@PathVariable int productNo) {
        productService.removeProduct(productNo);
        return ResponseEntity.ok().body("delete Success");
    }

}
