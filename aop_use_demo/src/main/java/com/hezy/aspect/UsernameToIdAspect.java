package com.hezy.aspect;

import com.hezy.annotation.FieldAnnotation;
import com.hezy.annotation.ParamAnnotation;
import com.hezy.mapper.UserMapper;
import com.hezy.pojo.UserDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
public class UsernameToIdAspect {

    @Autowired
    private UserMapper userMapper;

    @Around("@annotation(com.hezy.annotation.InterfaceAnnotation)")
    public Object resolveUsernameToId(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取方法参数列表
        Object[] args = joinPoint.getArgs();
        // 获取方法参数的注解，这里是二位数组，是因为一个参数可以有多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        // 遍历每个注解
        for (int i = 0; i < parameterAnnotations.length; i++) {
            // 遍历注解，判断是否有ParamAnnotation注解
            for (Annotation annotation : parameterAnnotations[i]) {
                // 如果有ParamAnnotation注解
                if (annotation instanceof ParamAnnotation) {
                    // 获取该对象的属性值
                    String username = (String) args[i];
                    // 将根据username查询后的accountId赋值给这个参数
                    args[i] = getUserIdByUsername(username);
                }
            }

            // 参数如果是对象
            if (args[i] instanceof UserDTO) {
                // 获取对象的所有属性，并遍历
                Field[] declaredFields = args[i].getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    // 如果有 FieldAnnotation 注解
                    if (field.getAnnotation(FieldAnnotation.class) != null) {
                        // 设置属性为可访问的
                        field.setAccessible(true);
                        // 获取该对象的属性值
                        String username = (String) field.get(args[i]);
                        // 将根据username查询后的accountId赋值给该对象的id字段
                        Field accountId = args[i].getClass().getDeclaredField("id");
                        accountId.setAccessible(true);
                        accountId.set(args[i], getUserIdByUsername(username));
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }

    /**
     * 根据username去查userId
     */
    private String getUserIdByUsername(String username) {
        String userId = userMapper.selectIdByUsername(username);
        if (userId == null) {
            throw new RuntimeException("操作失败，该账户不存在");
        }
        return userId;
    }
}