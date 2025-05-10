package com.github.zeroput.million_thumb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.mapper.BlogMapper;
import com.github.zeroput.million_thumb.model.entity.Blog;
import com.github.zeroput.million_thumb.model.entity.Thumb;
import com.github.zeroput.million_thumb.model.entity.User;
import com.github.zeroput.million_thumb.model.vo.BlogVO;
import com.github.zeroput.million_thumb.service.BlogService;
import com.github.zeroput.million_thumb.service.ThumbService;
import com.github.zeroput.million_thumb.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Arch
* @description 针对表【blog】的数据库操作Service实现
* @createDate 2025-05-09 21:42:41
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

    @Resource
    private UserService userService;

    @Lazy//解决循环引用
    @Resource
    private ThumbService thumbService;

    @Override
    public BlogVO getBlogVOById(HttpServletRequest request, Long id) {

        Blog blogEntity = this.getById(id);
        User currentUser = userService.getCurrentUser(request);

        BlogVO vo = this.getBlogVO(blogEntity, currentUser);

        return vo;
    }

    /**
     * entity转VO list
     * @param blogList
     * @param request
     * @return
     */
    @Override
    public List<BlogVO> getBlogVOList(List<Blog> blogList, HttpServletRequest request) {
        User loginUser = userService.getCurrentUser(request);
        Map<Long, Boolean> blogIdHasThumbMap = new HashMap<>();
        if (ObjUtil.isNotEmpty(loginUser)) {
            Set<Long> blogIdSet = blogList.stream().map(Blog::getId).collect(Collectors.toSet());
            // 获取点赞
            List<Thumb> thumbList = thumbService.lambdaQuery()
                    .eq(Thumb::getUserId, loginUser.getId())
                    .in(Thumb::getBlogId, blogIdSet)
                    .list();

            thumbList.forEach(blogThumb -> blogIdHasThumbMap.put(blogThumb.getBlogId(), true));
        }

        return blogList.stream()
                .map(blog -> {
                    BlogVO blogVO = BeanUtil.copyProperties(blog, BlogVO.class);
                    blogVO.setHasThumb(blogIdHasThumbMap.get(blog.getId()));
                    return blogVO;
                })
                .toList();
    }



    private BlogVO getBlogVO(Blog blogEntity, User currentUser) {
        BlogVO blogVO = new BlogVO();

        BeanUtil.copyProperties(blogEntity, blogVO);

        if (currentUser == null) {
            return blogVO;
        }

        // 用户登陆账号了
        Thumb thumbEntity = thumbService.lambdaQuery()
                .eq(Thumb::getUserId, currentUser.getId())
                .eq(Thumb::getBlogId, blogEntity.getId())
                .one();

        // 是否已经点赞
        blogVO.setHasThumb(thumbEntity != null);

        return blogVO;
    }



}




