package me.daniel.mapper;

import me.daniel.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    void insertTest(UserVO userVO);
}
