package com.daniel.controller.product;

import com.daniel.domain.DTO.product.ProductDTO;
import com.daniel.domain.DTO.product.ProductListDTO;
import com.daniel.enums.global.ResponseMessage;
import com.daniel.enums.products.ChickenBreast;
import com.daniel.enums.products.ChickenStatus;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.FileService;
import com.daniel.service.ProductService;
import com.daniel.service.RedisService;
import com.daniel.utility.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ExtendWith(RestDocumentationExtension.class)
@Import(value = {AuthorizationExtractor.class, JwtTokenProvider.class})
@AutoConfigureRestDocs
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    ProductService productService;

    @MockBean
    FileService fileService;

    @MockBean
    RedisService redisService;

    final ProductDTO productDTO = ProductDTO.builder()
            .productNo(100)
            .productName("테스트 상품")
            .productCategory(ChickenBreast.STEAMED.getValue())
            .productPrice(12222)
            .productStock(1400)
            .productDetail("건강한 맛입니다.")
            .productImage("http://localhost:8080/api/products/download/9b7c7e7a-b848-4d4f-b9a4-8f5d736e25da_red.png")
            .productStatus(ChickenStatus.SALE.getValue())
            .build();

    ProductListDTO listDTO1 = ProductListDTO.builder()
            .productNo(120)
            .productName("테스트 상품1")
            .productPrice(12222)
            .productStock(100)
            .productImage("http://localhost:8080/api/products/download/4b5a1c0c-6bef-449e-955c-e6d88e89ab70_red.png")
            .build();

    ProductListDTO listDTO2 = ProductListDTO.builder()
            .productNo(110)
            .productName("테스트 상품2")
            .productPrice(12222)
            .productStock(400)
            .productImage("http://localhost:8080/api/products/download/9b7c7e7a-b848-4d4f-b9a4-8f5d736e25da_red.png")
            .build();

    final List<ProductListDTO> productListDTOList = new ArrayList<>();

    final ProductDTO modifiedDTO = productDTO;

    final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ProductController(productService, fileService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .addFilters(new CharacterEncodingFilter("utf-8", true))
                        .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                        .build();
    }

    @Test
    @DisplayName("상품 조회 테스트")
    void productDetailTest() throws Exception {
        Mockito.when(productService.findByNumber(productDTO.getProductNo())).thenReturn(productDTO);
        Mockito.when(fileService.getDownloadURI(anyString())).thenReturn(productDTO.getProductImage());

        mockMvc.perform(get("/api/products/{productNo}", productDTO.getProductNo()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(productDTO)))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void productListTest() throws Exception {
        productListDTOList.add(listDTO1);
        productListDTOList.add(listDTO2);

        when(fileService.getDownloadURI(anyString())).thenReturn(productDTO.getProductImage());

        when(productService.getCategoryList(anyString(),
                anyString(),
                any(),
                anyInt()))
                .thenReturn(productListDTOList);

        mockMvc.perform(get("/api/products/list/" + ChickenBreast.STEAMED.getValue())
                        .param("pageNum", "1")
                        .param("searchKeyword", "product_name")
                        .param("searchValue", "스팀"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void productAddAction() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "red.png",
                MediaType.IMAGE_PNG_VALUE, "<<png data>>".getBytes());

        MockMultipartFile dto = new MockMultipartFile("productDTO", "productDTO",
                MediaType.APPLICATION_JSON_VALUE, JsonUtil.objectToString(productDTO).getBytes());

        given(productService.findByName(anyString())).willReturn(productDTO);

        given(fileService.uploadFile(any(MultipartFile.class))).willReturn("9b7c7e7a-b848-4d4f-b9a4-8f5d736e25da_red.png");

        given(fileService.getDownloadURI(anyString())).willReturn(productDTO.getProductImage());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/products");
        builder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });

        mockMvc.perform(builder
                        .file("image", image.getBytes())
                        .file(dto)
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productName", is(productDTO.getProductName())))
                .andExpect(jsonPath("$.message", is(ResponseMessage.ADD_MESSAGE.getValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void productModifyAction() throws Exception {
        modifiedDTO.setProductStock(1200);

        MockMultipartFile image = new MockMultipartFile("image", "red.png",
                MediaType.IMAGE_PNG_VALUE, "<<png data>>".getBytes());

        MockMultipartFile modifiedDto = new MockMultipartFile("productDTO", "productDTO",
                MediaType.APPLICATION_JSON_VALUE, JsonUtil.objectToString(modifiedDTO).getBytes());

        given(productService.findByNumber(modifiedDTO.getProductNo())).willReturn(modifiedDTO);

        given(fileService.uploadFile(image)).willReturn("9b7c7e7a-b848-4d4f-b9a4-8f5d736e25da_red.png");

        given(fileService.getDownloadURI(anyString())).willReturn(modifiedDTO.getProductImage());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/products");
        builder.with(request -> {
            request.setMethod(HttpMethod.PUT.name());
            return request;
        });

        mockMvc.perform(builder
                        .file("image", image.getBytes())
                        .file(modifiedDto)
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productStock", is(modifiedDTO.getProductStock())))
                .andExpect(jsonPath("$.message", is(ResponseMessage.MODIFY_MESSAGE.getValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void productDeleteAction() throws Exception {
        given(productService.findByNumber(productDTO.getProductNo())).willReturn(productDTO);

        mockMvc.perform(delete("/api/products/" + productDTO.getProductNo())
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(content().string(ResponseMessage.DELETE_MESSAGE.getValue()))
                .andDo(print());
    }
}