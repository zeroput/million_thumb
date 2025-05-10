package com.github.zeroput.million_thumb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.constant.ThumbConstant;
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
import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean doThumbAction(DoThumbRequestDto doThumbRequestDto, HttpServletRequest request) {
        if (doThumbRequestDto == null || doThumbRequestDto.getBlogId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误，请求的博客Id不可为空");
        }
        // 判断博客是否存在
        LambdaQueryChainWrapper<Blog> eq = blogService.lambdaQuery().eq(Blog::getId, doThumbRequestDto.getBlogId());
        Blog one = eq.one();

        if (one == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前博客找不到，请检查Id后重试");
        }

        User currentUser = userService.getCurrentUser(request);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "当前用户未登录");
        }
        // use intern
        synchronized (currentUser.getId().toString().intern()) {

            return transactionTemplate.execute(status -> {
                // 编程式事务 start
                Long blogId = doThumbRequestDto.getBlogId();
//                QueryWrapper<Thumb> wrapper = new QueryWrapper<Thumb>()
//                        .eq("userId", currentUser.getId())
//                        .eq("blogId", blogId);
//                boolean exists = this.baseMapper.exists(wrapper);  // query database
                boolean exists = this.hasThumbed(blogId, currentUser.getId()); // use cache
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
                boolean savedSuccess = this.save(thumb);
                if (savedSuccess) {
                    redisTemplate.opsForHash().put(ThumbConstant.USER_THUMB_KEY_PREFIX + currentUser.getId(), blogId.toString(),thumb.getId());
                }

                return updateOK && savedSuccess;
            });

        }
    }

    @Override
    public Boolean undoThumb(DoThumbRequestDto doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getBlogId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        // 判断博客是否存在
        LambdaQueryChainWrapper<Blog> eq = blogService.lambdaQuery().eq(Blog::getId, doThumbRequest.getBlogId());
        Blog one = eq.one();

        if (one == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前博客找不到，请检查Id后重试");
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
//                Thumb thumb = this.lambdaQuery()
//                        .eq(Thumb::getUserId, currentUser.getId())
//                        .eq(Thumb::getBlogId, blogId)
//                        .one();
//                if (thumb == null) {
//                    throw new RuntimeException("用户未点赞");
//                }

                // use cache
                Object thumbIdObj = redisTemplate.opsForHash().get(ThumbConstant.USER_THUMB_KEY_PREFIX + currentUser.getId(), blogId.toString());
                if (thumbIdObj == null) {
                    throw new RuntimeException("用户未点赞");
                }

                Long thumbId = Long.valueOf(thumbIdObj.toString());


                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount - 1")
                        .update();
                boolean success = update && this.removeById(thumbId);

                // 更新 并删除点赞记录完成
                if (success) {
                    redisTemplate.opsForHash().delete(ThumbConstant.USER_THUMB_KEY_PREFIX + currentUser.getId(), blogId.toString());

                }

                return success;
            });
        }
    }

    /**
     * 基于用户角度，选择 hash 结构存储。key 为用户 id，field为博客 id，value为 点赞记录 id
     * @param blogId
     * @param userId
     * @return
     */
    @Override
    public Boolean hasThumbed(Long blogId, Long userId) {
        // 数据示例  value为 点赞记录 id
        // thumbed-userId1 - blog01 value1
        //                 - blog02 value2
        //                 - blog03 value3
        return redisTemplate.opsForHash().hasKey(ThumbConstant.USER_THUMB_KEY_PREFIX + userId, blogId.toString());
    }


}




