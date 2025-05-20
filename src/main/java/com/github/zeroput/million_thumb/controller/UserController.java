package com.github.zeroput.million_thumb.controller;

import com.github.zeroput.million_thumb.common.BaseResponse;
import com.github.zeroput.million_thumb.common.ResultUtils;
import com.github.zeroput.million_thumb.constant.UserConstant;
import com.github.zeroput.million_thumb.model.entity.User;
import com.github.zeroput.million_thumb.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {  
    @Resource
    private UserService userService;
  
    @GetMapping("/login")
    public BaseResponse<User> login(long userId, HttpServletRequest request) {
        User user = userService.getById(userId);  
        request.getSession().setAttribute(UserConstant.LOGIN_USER, user);
        return ResultUtils.success(user);
    }

    @GetMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.LOGIN_USER);
        return ResultUtils.success("登出成功");
    }

    @GetMapping("/get/currenLogin")
    public BaseResponse<User> getLoginUser(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(UserConstant.LOGIN_USER);
        return ResultUtils.success(loginUser);
    }

}
