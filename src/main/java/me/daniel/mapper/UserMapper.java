package me.daniel.mapper;

import me.daniel.domain.VO.UserVO;
import me.daniel.utility.Pager;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

    UserVO selectUser(String userId);

    List<UserVO> selectUserList(Pager pager);

    void insertUser(UserVO userVO);

    void updateUser(UserVO userVO);

    void changeGradeUser(Map<String, Object> map);

}
