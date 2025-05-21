package com.github.zeroput.million_thumb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.constant.ThumbConstant;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Resource(name = "thumbDefaultService")
    private ThumbService thumbService;


    private final RedisTemplate<String, Object> redisTemplate;

    public BlogServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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

            // region 之前查数据库的方式
            // 这里面 是Long类型的
//            List<Object> collectBlogIds = blogList.stream().map(Blog::getId).collect(Collectors.toList());
            // 根据博客Id集合 从缓存中获取 点赞记录表
//            List<Object> thumbList = redisTemplate.opsForHash().multiGet(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), collectBlogIds);
//
//            for (Object thumbId : thumbList) {
//                if (thumbId == null) {
//                    continue;
//                }
//                blogIdHasThumbMap.put(Long.parseLong(thumbId.toString()), Boolean.TRUE);
//
//            }
            // 获取点赞
//            List<Object> thumbList = redisTemplate.opsForHash().multiGet(ThumbConstant.USER_THUMB_KEY_PREFIX + String.valueOf(loginUser.getId()), collectBlogIds);
//            for (int i = 0; i < thumbList.size(); i++) {
//                if (thumbList.get(i) == null) {
//                    continue;
//                }
//                blogIdHasThumbMap.put(Long.valueOf(collectBlogIds.get(i).toString()), true);
//            }
            // endregion

            // 在这里 blog.getId()调用呢,toString()方法 之前没调用 所以报错
            List<Object> blogIdList = blogList.stream().map(blog -> blog.getId().toString()).collect(Collectors.toList());
            // 获取点赞
            List<Object> thumbList = redisTemplate.opsForHash().multiGet(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogIdList);
            for (int i = 0; i < thumbList.size(); i++) {
                if (thumbList.get(i) == null) {
                    continue;
                }
                blogIdHasThumbMap.put(Long.valueOf(blogIdList.get(i).toString()), true);
            }

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

        // 从数据库查询点赞记录 改为从缓存中取 减少DB query 优化性能
//        Thumb thumbEntity = thumbService.lambdaQuery()
//                .eq(Thumb::getUserId, currentUser.getId())
//                .eq(Thumb::getBlogId, blogEntity.getId())
//                .one();
        // 是否已经点赞
//        blogVO.setHasThumb(thumbEntity != null);

        Boolean thumbed = thumbService.hasThumbed(blogEntity.getId(), currentUser.getId());
        blogVO.setHasThumb(thumbed);

        return blogVO;
    }



}




