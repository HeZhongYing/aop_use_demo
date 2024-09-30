package com.hezy.pojo;

import com.hezy.annotation.FieldAnnotation;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String id;

    @FieldAnnotation
    private String username;

    private String password;
}
