package com.github.zeroput.million_thumb.controller;

import com.github.zeroput.million_thumb.common.BaseResponse;
import com.github.zeroput.million_thumb.common.ResultUtils;
import com.github.zeroput.million_thumb.model.entity.Blog;
import com.github.zeroput.million_thumb.model.vo.BlogVO;
import com.github.zeroput.million_thumb.service.BlogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {
    @Resource
    private BlogService blogService;

    @GetMapping("/get")
    public BaseResponse<BlogVO> get(long blogId, HttpServletRequest request) {
        BlogVO blogVO = blogService.getBlogVOById(request, blogId);
        return ResultUtils.success(blogVO);
    }


    @GetMapping("/list")
    public BaseResponse<List<BlogVO>> listAll(HttpServletRequest request) {
        List<Blog> list = blogService.list();
        List<BlogVO> blogVOList = blogService.getBlogVOList(list, request);
        blogVOList.forEach(blogVO -> {
           if( blogVO.getHasThumb() == null){
               blogVO.setHasThumb(false);
           }
        });

        return ResultUtils.success(blogVOList);

    }
}
