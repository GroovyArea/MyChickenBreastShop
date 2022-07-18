package com.daniel.controller.product;

import com.daniel.domain.dto.product.ProductDTO;
import com.daniel.domain.dto.product.ProductListDTO;
import com.daniel.enums.global.ResponseMessage;
import com.daniel.enums.products.ChickenStatus;
import com.daniel.interceptor.auth.Auth;
import com.daniel.response.Message;
import com.daniel.service.FileService;
import com.daniel.service.ProductService;
import com.daniel.utility.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 상품 관련 Controller <br>
 * 상품 조회, 추가, 수정, 삭제 요청
 *
 * @author Nam Young Kim
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

    private static final String FAILED_FILE_CONTENT = "파일 형식을 결정할 수 없습니다.";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String FILE_REQUEST_MESSAGE = "파일을 넣어주세요.";

    private static final int PRODUCT_SIZE = 5;
    private static final int BLOCK_SIZE = 8;

    private final ProductService productService;
    private final FileService fileService;

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
    public ResponseEntity<ProductDTO> productDetail(@PathVariable int productNo) {
        ProductDTO selectedProduct = productService.findByNumber(productNo);

        String uploadFileName = selectedProduct.getProductImage();

        String downloadURI = fileService.getDownloadURI(uploadFileName);

        selectedProduct.setProductImage(downloadURI);

        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_JSON)
                .body(selectedProduct);
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
                .body(productService.getCategoryList(searchKeyword, searchValue,
                                new Pager(Pager.getStartRow(pageNum, PRODUCT_SIZE), BLOCK_SIZE), productCategoryNo).stream()
                        .map(selectedProduct -> {
                            String uploadFileName = selectedProduct.getProductImage();
                            String downloadURI = fileService.getDownloadURI(uploadFileName);
                            selectedProduct.setProductImage(downloadURI);
                            return selectedProduct;
                        })
                        .collect(Collectors.toList()));
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> getDownloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = fileService.loadFile(fileName);

        String contentType;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error(FAILED_FILE_CONTENT);
            throw new IOException(FAILED_FILE_CONTENT);
        }

        if (contentType == null) {
            contentType = DEFAULT_CONTENT_TYPE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename())
                .body(resource);
    }

    /**
     * 상품 추가
     *
     * @param productDTO 추가할 상품 정보
     * @return Message 응답 객체
     */
    @Auth(role = Auth.Role.ADMIN)
    @PostMapping
    public ResponseEntity<Object> addAction(@RequestPart ProductDTO productDTO,
                                            @RequestPart("image") MultipartFile file) throws IOException {
        if (file == null) {
            return ResponseEntity.badRequest().body(FILE_REQUEST_MESSAGE);
        }

        String uploadFileName = fileService.uploadFile(file);

        productDTO.setProductImage(uploadFileName);

        productService.addProduct(productDTO);

        ProductDTO savedProduct = productService.findByName(productDTO.getProductName());

        setURI(savedProduct, uploadFileName);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(savedProduct)
                        .message(ResponseMessage.ADD_MESSAGE.getValue())
                        .build()
        );
    }

    /**
     * 상품 수정
     *
     * @param productDTO 수정할 상품 정보
     * @return Message 응답 객체
     */
    @Auth(role = Auth.Role.ADMIN)
    @PutMapping
    public ResponseEntity<Message> modifyAction(@RequestPart ProductDTO productDTO,
                                                @RequestPart("image") MultipartFile file) throws IOException {
        if (file == null) {
            productService.modifyProduct(productDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                    Message.builder()
                            .data(productService.findByNumber(productDTO.getProductNo()))
                            .message(ResponseMessage.MODIFY_MESSAGE.getValue())
                            .build());
        }

        ProductDTO savedProductDTO = productService.findByNumber(productDTO.getProductNo());
        String savedImageName = savedProductDTO.getProductImage();

        fileService.deleteFile(savedImageName);

        String uploadFileName = fileService.uploadFile(file);
        productDTO.setProductImage(uploadFileName);

        productService.modifyProduct(productDTO);

        ProductDTO modifiedProduct = productService.findByNumber(productDTO.getProductNo());
        setURI(modifiedProduct, uploadFileName);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(modifiedProduct)
                        .message(ResponseMessage.MODIFY_MESSAGE.getValue())
                        .build()
        );
    }

    /**
     * 상품 삭제 (status 0으로 값 변경)
     *
     * @param productNo 삭제할 상품 번호
     * @return ResponseEntity Success
     */
    @Auth(role = Auth.Role.ADMIN)
    @DeleteMapping("/{productNo}")
    public ResponseEntity<Object> deleteAction(@PathVariable int productNo) throws IOException {
        String productImageName = productService.findByNumber(productNo).getProductImage();

        fileService.deleteFile(productImageName);

        deleteProductMap.put("productNo", productNo);
        deleteProductMap.put("productStatus", ChickenStatus.EXTINCTION.getValue());
        productService.removeProduct(deleteProductMap);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

    private void setURI(ProductDTO product, String uploadFileName) {
        String downloadURI = fileService.getDownloadURI(uploadFileName);
        product.setProductImage(downloadURI);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> ioExceptionHandle(IOException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
