package com.hezy.controller;

import com.hezy.annotation.FieldAnnotation;
import com.hezy.annotation.InterfaceAnnotation;
import com.hezy.annotation.ParamAnnotation;
import com.hezy.mapper.UserMapper;
import com.hezy.pojo.User;
import com.hezy.pojo.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @InterfaceAnnotation
    @GetMapping("{id}")
    public User getUser(@ParamAnnotation @PathVariable String id) {
        return userMapper.selectUserById(id);
    }

    @InterfaceAnnotation
    @DeleteMapping
    public void deleteUserByUsername(@RequestBody UserDTO userDTO) {
        userMapper.deleteUserByUsername(userDTO);
    }
}
