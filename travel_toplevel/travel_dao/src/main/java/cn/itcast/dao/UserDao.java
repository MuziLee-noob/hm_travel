package cn.itcast.dao;

import cn.itcast.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    //查找所有用户
    @Select("select * from tab_user")
    List<User> findAll();

    //根据用户名查找用户
    @Select("select * from tab_user where username = #{username}")
    List<User> findUserByName(String username);

    //保存用户
    @Insert("insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(#{username},#{password},#{name},#{birthday},#{sex},#{telephone},#{email},#{status},#{code})")
    void saveUser(User user);

    //登录
    @Select("select * from tab_user where username = #{username} and password = #{password}")
    User login(@Param("username") String username, @Param("password") String password);

    //根据激活码查询用户对象
    @Select("select * from tab_user where code = #{code}")
    User findByCode(String code);

    //修改指定用户激活状态
    @Update("update tab_user set status = 'Y' where uid = #{uid}")
    void updateUserStatus(User user);
}
