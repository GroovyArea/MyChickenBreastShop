package me.daniel.controller.user;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.VO.UserVO;
import me.daniel.exception.UserExistsException;
import me.daniel.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 처리
     * @param joinUser
     * @return UserVO
     * @throws UserExistsException
     */
    @PostMapping("/join")
    public ResponseEntity joinAction(@ModelAttribute UserDTO joinUser) throws UserExistsException {
        userService.addUser(joinUser);
        return ResponseEntity.ok().body(userService.getUser(joinUser.getUserId()));
    }
}
