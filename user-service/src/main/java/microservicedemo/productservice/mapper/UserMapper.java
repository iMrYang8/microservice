package microservicedemo.productservice.mapper;

import microservicedemo.productservice.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    int insert(User user); // 注册

    @Select("SELECT id, username, password FROM user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username); // 登录
}
