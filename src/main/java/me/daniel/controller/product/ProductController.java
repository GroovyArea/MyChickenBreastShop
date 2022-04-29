package me.daniel.controller.product;

import lombok.RequiredArgsConstructor;
import me.daniel.service.ProductService;
import me.daniel.utility.Pager;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // 1. 상품 디테일 요청(상품 번호로 쿼리 스트링으로 날리기) => get?
    @GetMapping("/detail")
    public String detailPage(@RequestParam int productNo, Model model){

        // 상품 단일 행 반환
        model.addAttribute(productService.getProduct(productNo));

        return "/fragments/product/product_detail";
    }

    // 2. 카테고리 별 상품 리스트 요청(스팀, 훈제, 저염, 스테이크, 큐브, 생닭가슴살)
    // 페이징 처리 데이터(페이지 번호, 총 개수), 상품 카테고리 번호
    @GetMapping("/list/{productCategoryNo}")
    public String listPage(@PathVariable(value = "productCategoryNo") int productCategoryNo,
                           @RequestParam(defaultValue = "1") int pageNum,
                           Model model){


        // 페이저 인자로 4개 넘기기
        // 전체 게시글 갯수 sql 생성
        // pageSize = 10
        // blockSize = 5
        //Pager pager = new Pager();

        return ""; // 카테고리별 페이지 get 요청 건 만들기 if문 사용?
    }

}
