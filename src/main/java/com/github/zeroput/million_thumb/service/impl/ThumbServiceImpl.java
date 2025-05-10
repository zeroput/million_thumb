package com.github.zeroput.million_thumb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.exception.BusinessException;
import com.github.zeroput.million_thumb.exception.ErrorCode;
import com.github.zeroput.million_thumb.mapper.ThumbMapper;
import com.github.zeroput.million_thumb.model.dto.thumb.DoThumbRequestDto;
import com.github.zeroput.million_thumb.model.entity.Blog;
import com.github.zeroput.million_thumb.model.entity.Thumb;
import com.github.zeroput.million_thumb.model.entity.User;
import com.github.zeroput.million_thumb.service.BlogService;
import com.github.zeroput.million_thumb.service.ThumbService;
import com.github.zeroput.million_thumb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
* @author Arch
* @description 针对表【thumb】的数据库操作Service实现
* @createDate 2025-05-09 21:42:41
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb>
    implements ThumbService {

    private final BlogService blogService;
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Boolean doThumbAction(DoThumbRequestDto doThumbRequestDto, HttpServletRequest request) {
        if (doThumbRequestDto == null || doThumbRequestDto.getBlogId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误，请求的博客Id不可为空");
        }

        User currentUser = userService.getCurrentUser(request);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "当前用户未登录");
        }
        // use intern
        synchronized (currentUser.getId().toString().intern()) {

            transactionTemplate.execute(status -> {
                // 编程式事务 start
                Long blogId = doThumbRequestDto.getBlogId();
                QueryWrapper<Thumb> wrapper = new QueryWrapper<Thumb>()
                        .eq("userId", currentUser.getId())
                        .eq("blogId", blogId);

                boolean exists = this.baseMapper.exists(wrapper);

                if (exists) {
                    throw new RuntimeException("用户已点赞");
                }

                // update

                boolean updateOK = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount + 1")
                        .update();

                Thumb thumb = new Thumb();
                thumb.setUserId(currentUser.getId());
                thumb.setBlogId(blogId);

//                int insert = this.baseMapper.insert(thumb);
                boolean save = this.save(thumb);

                return updateOK && save;
            });

        }

        return null;
    }

    @Override
    public Boolean undoThumb(DoThumbRequestDto doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getBlogId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        User currentUser = userService.getCurrentUser(request);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "当前用户未登录");
        }
        // 加锁
        synchronized (currentUser.getId().toString().intern()) {

            // 编程式事务
            return transactionTemplate.execute(status -> {
                Long blogId = doThumbRequest.getBlogId();
                Thumb thumb = this.lambdaQuery()
                        .eq(Thumb::getUserId, currentUser.getId())
                        .eq(Thumb::getBlogId, blogId)
                        .one();
                if (thumb == null) {
                    throw new RuntimeException("用户未点赞");
                }
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount - 1")
                        .update();

                return update && this.removeById(thumb.getId());
            });
        }
    }


}




