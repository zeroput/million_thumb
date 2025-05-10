package com.github.zeroput.million_thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zeroput.million_thumb.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author Arch
* @description 针对表【user】的数据库操作Service
* @createDate 2025-05-09 21:42:41
*/
public interface UserService extends IService<User> {

    public User getCurrentUser(HttpServletRequest request);
}
