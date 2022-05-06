package me.daniel.controller.user;

import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 회원 Controller
 * 회원 조회, 수정, 삭제, 로그인, 로그아웃 요청
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * login 처리
     * @param userVO
     * @param session
     * @return "login Success"
     */
    @PostMapping("/login")
    public String loginAction(@ModelAttribute UserVO userVO, HttpSession session) {
        session.setAttribute("loginUser", userVO);
        return "login Success";
    }

    /**
     * logout 처리
     * @param session
     * @return "logout Success"
     */
    @GetMapping("/logout")
    public String logoutAction(HttpSession session) {
        session.invalidate();
        return "logout Success";
    }

    /**
     *
     * @param userId 회원 아이디
     * @return 회원 정보
     */
    @GetMapping("/detail/{userId}")
    public UserVO detailAction(@PathVariable String userId){
       UserVO returnUser = userService.getUser(userId);
       returnUser.setUserPw("암호화");
        return returnUser;
    }

    /**
     * 회원 정보 수정
     * @param userVO 수정할 회원 정보
     * @return UserVO 수정된 회원 정보
     */
    @PostMapping("/modify")
    public UserVO modifyAction(@ModelAttribute UserVO userVO) {
        userService.modifyUser(userVO);
        UserVO returnUser = userService.getUser(userVO.getUserId());
        returnUser.setUserPw("암호화");
        return returnUser;
    }

    /**
     * 회원 탈퇴 처리
     * @param userId 탈퇴할 회원 아이디
     * @return delete Success
     */
    @GetMapping("/delete/{userId}")
    public String deleteAction(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "delete Success";
    }

}
