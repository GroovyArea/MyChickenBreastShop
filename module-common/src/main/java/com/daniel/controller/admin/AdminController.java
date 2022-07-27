package com.daniel.controller.admin;

import com.daniel.domain.dto.user.UserListDTO;
import com.daniel.interceptor.auth.Auth;
import com.daniel.service.UserService;
import com.daniel.utility.Pager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자 컨트롤러 <br>
 * 관리자의 기능을 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.31 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    //TODO 컨트롤러에 꼭 있어야 하는 값들인지 고민해보면 좋을거같아요
    private static final String SUCCEED_CHANGE_GRADE = "회원 등급 변경이 완료되었습니다.";
    private static final int USERS_SIZE = 5;
    private static final int BLOCK_SIZE = 10;

    private final UserService userService;

    /**
     * 회원 등급 변경
     *
     * @param userId    회원 아이디
     * @param userGrade 회원 등급
     * @return 상태 변경 메시지
     */
    @Auth(role = Auth.Role.ADMIN)
    @PatchMapping("/user/{userId}/grade/{userGrade}")
    public ResponseEntity<String> userGradeChange(@PathVariable String userId, @PathVariable int userGrade) { //TODO userGrade를 int가 아니라 enum으로 관리하는게 더 좋아보여요
        Map<String, Object> changeUserGradeMap = new HashMap<>(); //TODO 모델 대신 Map을 사용하는 걸 지양해주세요. 편리하지만 서비스를 이해하기 어렵게 만듭니다.
        changeUserGradeMap.put("userId", userId);
        changeUserGradeMap.put("userGrade", userGrade);
        userService.changeGradeUser(changeUserGradeMap);
        return ResponseEntity.ok().body(SUCCEED_CHANGE_GRADE); //TODO return SUCCEED_CHANGE_GRADE; 로 해도 충분하지 않나요? status 어차피 200응답일테니까요
    }

    /**
     * 회원 목록 검색
     *
     * @param pageNum       현재 페이지 번호
     * @param searchKeyword 검색 키워드
     * @param searchValue   검색 값
     * @return 검색 리스트
     */
    @Auth(role = Auth.Role.ADMIN)
    @GetMapping("/users")
    public ResponseEntity<List<UserListDTO>> userSearchList(@RequestParam(defaultValue = "1") int pageNum,
                                                            @RequestParam(defaultValue = "user_name") String searchKeyword,
                                                            @RequestParam String searchValue) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUserList(searchKeyword, searchValue, new Pager(Pager.getStartRow(pageNum, USERS_SIZE), BLOCK_SIZE))); //TODO 이미 만들어진걸 활용하면 좋을거같아요, Pageable 참고
    }
}
