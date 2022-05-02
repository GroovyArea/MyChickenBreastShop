package me.daniel.mapper;

import me.daniel.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    UserVO selectUser(int userNo);

    void insertUser(UserVO userVO);

}
