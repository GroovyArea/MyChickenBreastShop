package me.daniel.controller.product;

import me.daniel.domain.DTO.ProductListDTO;
import me.daniel.domain.DTO.ProductModifyDTO;
import me.daniel.enums.global.ResponseMessage;
import me.daniel.enums.products.ChickenStatus;
import me.daniel.interceptor.auth.Auth;
import me.daniel.responseMessage.Message;
import me.daniel.service.ProductService;
import me.daniel.utility.Pager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 관련 Controller <br>
 * 상품 조회, 추가, 수정, 삭제 요청
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final int PRODUCT_SIZE = 5;
    private static final int BLOCK_SIZE = 8;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * page 당 게시글 리스트를 반환 받기 위한 map 객체
     */
    private final Map<String, Object> pagerMap = new HashMap<>();
    /**
     * 상품 삭제 정보를 담기 위한 map
     */
    private final Map<String, Object> deleteProductMap = new HashMap<>();


    /**
     * 단일 상품 디테일 객체를 반환하는 메서드
     *
     * @param productNo 상품 번호
     * @return ResponseEntity 상품 정보
     */
    @GetMapping("/{productNo}")
    public ResponseEntity<ProductListDTO> productDetail(@PathVariable int productNo) {
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
    public ResponseEntity<List<ProductListDTO>> productList(@PathVariable(value = "productCategoryNo") int productCategoryNo,
                                                            @RequestParam(defaultValue = "1") int pageNum,
                                                            @RequestParam(required = false) String searchKeyword,
                                                            @RequestParam(required = false) String searchValue) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getCategoryList(new Pager(searchKeyword, searchValue, Pager.getStartRow(pageNum, PRODUCT_SIZE), BLOCK_SIZE), productCategoryNo));
    }

    /**
     * 상품 추가
     *
     * @param productDTO 추가할 상품 정보
     * @return Message 응답 객체
     */
    @Auth(role = Auth.Role.ADMIN)
    @PostMapping
    public Message addAction(@ModelAttribute ProductListDTO productDTO) {
        productService.addProduct(productDTO);
        return new Message
                .Builder(productService.findByName(productDTO.getProductName()))
                .message(ResponseMessage.ADD_MESSAGE.getValue())
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 상품 수정
     *
     * @param productModifyDTO 수정할 상품 정보
     * @return Message 응답 객체
     */
    @Auth(role = Auth.Role.ADMIN)
    @PutMapping
    public Message modifyAction(@ModelAttribute ProductModifyDTO productModifyDTO) {
        productService.modifyProduct(productModifyDTO);
        return new Message
                .Builder(productService.findByNumber(productModifyDTO.getProductNo()))
                .message(ResponseMessage.MODIFY_MESSAGE.getValue())
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 상품 삭제 (status 0으로 값 변경)
     *
     * @param productNo 삭제할 상품 번호
     * @return ResponseEntity Success
     */
    @Auth(role = Auth.Role.ADMIN)
    @DeleteMapping("/{productNo}")
    public ResponseEntity<String> deleteAction(@PathVariable int productNo) {
        deleteProductMap.put("productNo", productNo);
        deleteProductMap.put("productStatus", ChickenStatus.EXTINCTION.getValue());
        productService.removeProduct(deleteProductMap);
        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }
}
