package me.daniel.controller.product;

import lombok.RequiredArgsConstructor;
import me.daniel.Enum.ChickenStatus;
import me.daniel.service.ProductService;
import me.daniel.utility.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // 1. 상품 디테일 요청(상품 번호로 쿼리 스트링으로 날리기) => get?
    @GetMapping("/detail/{productNo}")
    public String detailPage(@PathVariable int productNo, Model model){

        // 상품 단일 행 반환
        model.addAttribute("product",productService.getProduct(productNo));

        return "/fragments/product/product_detail";
    }

    // 2. 카테고리 별 상품 리스트 요청(스팀, 훈제, 저염, 스테이크, 큐브, 생닭가슴살)
    // 페이징 처리 데이터(페이지 번호, 총 개수), 상품 카테고리 번호
    @GetMapping("/list/{productCategoryNo}")
    public String listPage(@PathVariable(value = "productCategoryNo") int productCategoryNo,
                           @RequestParam(defaultValue = "1") int pageNum,
                           Model model){

        Map<String, Object> countMap = new HashMap<>();

        countMap.put("productCategory", productCategoryNo);
        countMap.put("productStatus", ChickenStatus.SALE.getValue());

        int totalProduct = productService.getCategoryCount(countMap);
        int productSize = 5;
        int blockSize = 6;
        //int number = totalProduct - (pageNum-1) * productSize;

        // 페이징 객체
        Pager pager = new Pager(pageNum, totalProduct, productSize, blockSize);

        Map<String, Object> pagerMap = new HashMap<>();
        pagerMap.put("productCategory", productCategoryNo);
        pagerMap.put("startRow",pager.getStartRow()-1);
        pagerMap.put("rowCount", blockSize);

        model.addAttribute("productList", productService.getCategoryList(pagerMap));

        return "/fragments/product/product_list"; // 카테고리별 페이지 get 요청 건 만들기 if문 사용?
    }

}
