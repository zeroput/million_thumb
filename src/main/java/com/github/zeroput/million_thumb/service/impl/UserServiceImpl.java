package com.github.zeroput.million_thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.constant.UserConstant;
import com.github.zeroput.million_thumb.mapper.UserMapper;
import com.github.zeroput.million_thumb.model.entity.User;
import com.github.zeroput.million_thumb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
* @author Arch
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-05-09 21:42:41
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


    @Override
    public User getCurrentUser(HttpServletRequest request) {

        Object myAttr = request.getSession().getAttribute(UserConstant.LOGIN_USER);
        if (myAttr != null) {
            return (User) myAttr;
        }
        return null;
    }
}




