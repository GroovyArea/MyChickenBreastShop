package me.daniel.controller.product;

import me.daniel.domain.DTO.ProductDTO;
import me.daniel.domain.DTO.ProductModifyDTO;
import me.daniel.enums.ResponseMessage;
import me.daniel.service.ProductService;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private static final int PRODUCT_SIZE = 5;
    private static final int BLOCK_SIZE = 8;

    /**
     * page 당 게시글 리스트를 반환 받기 위한 map 객체
     */
    private Map<String, Object> pagerMap = new HashMap<>();

    /**
     * 단일 상품 디테일 객체를 반환하는 메서드
     *
     * @param productNo 상품 번호
     * @return ResponseEntity 상품 정보
     */
    @GetMapping("/{productNo}")
    public ResponseEntity productDetail(@PathVariable int productNo) {
        return ResponseEntity.ok().body(productService.findByNumber(productNo));
    }

    /**
     * page 당 상품 리스트 반환하는 메서드
     *
     * @param productCategoryNo 상품 카테고리 번호
     * @param pageNum           페이지 번호
     * @return ResponseEntity 페이지 정보, 상품 카테고리 리스트
     */
    @GetMapping("/list/{productCategoryNo}")
    public ResponseEntity productList(@PathVariable(value = "productCategoryNo") int productCategoryNo, @RequestParam(defaultValue = "1") int pageNum) {
        pagerMap.put("productCategory", productCategoryNo);
        pagerMap.put("startRow", getStartRow(pageNum) - 1);
        pagerMap.put("rowCount", BLOCK_SIZE);

        return ResponseEntity.ok().body(productService.getCategoryList(pagerMap));
    }

    /**
     * 상품 추가
     *
     * @param productDTO 추가할 상품 정보
     * @return ResponseEntity 추가된 상품 정보
     */
    @PostMapping
    public ResponseEntity addAction(@ModelAttribute ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return ResponseEntity.ok().body(productService.findByName(productDTO.getProductName()));
    }

    /**
     * 상품 수정
     *
     * @param productModifyDTO 수정할 상품 정보
     * @return ResponseEntity 수정된 상품 정보
     */
    @PutMapping
    public ResponseEntity modifyAction(@ModelAttribute ProductModifyDTO productModifyDTO) {
        productService.modifyProduct(productModifyDTO);
        return ResponseEntity.ok().body(productService.findByNumber(productModifyDTO.getProductNo()));
    }

    /**
     * 상품 삭제 (status 0으로 값 변경)
     *
     * @param productNo 삭제할 상품 번호
     * @return ResponseEntity Success
     */
    @DeleteMapping("/{productNo}")
    public ResponseEntity deleteAction(@PathVariable int productNo) {
        productService.removeProduct(productNo);
        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

    /**
     * 상품 리스트 시작 행 구하기
     * @param pageNum 현재 페이지 번호
     * @return startRow 시작 행
     */
    public static int getStartRow(int pageNum) {
        return (pageNum - 1) * PRODUCT_SIZE + 1;
    }
}
