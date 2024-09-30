package com.hezy.mapper;

import com.hezy.pojo.User;
import com.hezy.pojo.UserDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from i_users where id = #{id}")
    User selectUserById(@Param("id") String id);

    @Select("select id from i_users where username = #{username}")
    String selectIdByUsername(@Param("username") String username);

    @Delete("delete from i_users where id = #{userDTO.id}")
    void deleteUserByUsername(@Param("userDTO") UserDTO userDTO);
}
