package me.daniel.controller.product;

import lombok.RequiredArgsConstructor;
import me.daniel.Enum.ChickenStatus;
import me.daniel.domain.ProductVO;
import me.daniel.service.ProductService;
import me.daniel.utility.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private Map<String, Object> countMap = new HashMap<>();

    private Map<String, Object> pagerMap = new HashMap<>();

    // 1. 상품 디테일 요청(상품 번호로 쿼리 스트링으로 날리기) => get?
    @GetMapping("/detail/{productNo}")
    public String detailPage(@PathVariable int productNo, Model model) {

        // 상품 단일 행 반환
        model.addAttribute("product", productService.getProduct(productNo));

        return "/fragments/product/product_detail";
    }

    // 2. 카테고리 별 상품 리스트 요청(스팀, 훈제, 소시지, 스테이크, 볼, 생닭가슴살)
    // 페이징 처리 데이터(페이지 번호, 총 개수), 상품 카테고리 번호
    @GetMapping("/list/{productCategoryNo}")
    public String listPage(@PathVariable(value = "productCategoryNo") int productCategoryNo,
                           @RequestParam(defaultValue = "1") int pageNum,
                           Model model) {

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

        List<ProductVO> list = productService.getCategoryList(pagerMap);
        List<ProductVO> list1 = new ArrayList<>();
        List<ProductVO> list2 = new ArrayList<>();

        if (list.size() < 4) {
            model.addAttribute("productList1", list);
        } else if (list.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                list1.add(list.get(i));
                model.addAttribute("productList1", list1);
            }
        }

        if (list.size() >= 5) {
            for (int a = 4; a < list.size(); a++) {
                list2.add(list.get(a));
                model.addAttribute("productList2", list2);
            }
        }

        if (productCategoryNo == 1) {
            return "/fragments/product/product_list";
        } else if (productCategoryNo == 2) {
            return "/fragments/product/product_list_smoked";
        } else if (productCategoryNo == 3) {
            return "/fragments/product/product_list_sausage";
        } else if (productCategoryNo == 4) {
            return "/fragments/product/product_list_steak";
        } else if (productCategoryNo == 5) {
            return "/fragments/product/product_list_ball";
        } else {
            return "/fragments/product/product_list_raw";
        }
        // 카테고리별 페이지 get 요청 건 만들기 if문 사용?
    }

    // 상품 추가, 수정, 삭제 핸들러

}
