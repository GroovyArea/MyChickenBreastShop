package me.daniel.mapper;

import me.daniel.domain.VO.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    UserVO selectUser(String userId);

    void insertUser(UserVO userVO);

    void updateUser(UserVO userVO);

    void deleteUser(String userId);

}
