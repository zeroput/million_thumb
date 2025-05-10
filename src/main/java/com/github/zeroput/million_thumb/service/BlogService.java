package com.github.zeroput.million_thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zeroput.million_thumb.model.entity.Blog;
import com.github.zeroput.million_thumb.model.vo.BlogVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author Arch
* @description 针对表【blog】的数据库操作Service
* @createDate 2025-05-09 21:42:41
*/
public interface BlogService extends IService<Blog> {

    BlogVO getBlogVOById(HttpServletRequest request, Long id);

    List<BlogVO> getBlogVOList(List<Blog> blogList ,HttpServletRequest request);
}
