package me.daniel.mapper;

import me.daniel.domain.ProductVO;
import me.daniel.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ProductMapper {

    ProductVO selectProduct(int productNo);
}
