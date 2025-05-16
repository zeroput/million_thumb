package com.github.zeroput.million_thumb.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.common.RedisKeyUtil;
import com.github.zeroput.million_thumb.constant.RedisLuaScriptConstant;
import com.github.zeroput.million_thumb.constant.ThumbConstant;
import com.github.zeroput.million_thumb.exception.BusinessException;
import com.github.zeroput.million_thumb.exception.ErrorCode;
import com.github.zeroput.million_thumb.mapper.ThumbMapper;
import com.github.zeroput.million_thumb.model.dto.thumb.DoThumbRequestDto;
import com.github.zeroput.million_thumb.model.entity.Thumb;
import com.github.zeroput.million_thumb.model.entity.User;
import com.github.zeroput.million_thumb.model.enums.LuaExecuteStatusEnum;
import com.github.zeroput.million_thumb.service.ThumbService;
import com.github.zeroput.million_thumb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* @author Arch
* @description 针对表【thumb】的数据库操作Service实现
* @createDate 2025-05-09 21:42:41
*/
@Service("thumbRedisService")
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceRedisImpl extends ServiceImpl<ThumbMapper, Thumb>
    implements ThumbService {

    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean doThumbAction(DoThumbRequestDto doThumbRequestDto, HttpServletRequest request) {
        validateTheParams(doThumbRequestDto);

        User currentUser = userService.getCurrentUser(request);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "当前用户未登录");
        }
        Long blogId = doThumbRequestDto.getBlogId();

        String timeSlice = getTimeSlice();

        // Redis key
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(timeSlice);//示例  thumb:temp:13:01:20
        String userThumbKey = RedisKeyUtil.getUserThumbKey(currentUser.getId()); // thumb_userId_2

        //  执行脚本会返回一个long类型数字
        Long executeResultLong = redisTemplate.execute(RedisLuaScriptConstant.THUMB_SCRIPT,
                Arrays.asList(tempThumbKey, userThumbKey),
                currentUser.getId(),
                blogId
        );

        if (LuaExecuteStatusEnum.FAIL.getValue() == executeResultLong) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已点赞");
        }

        return executeResultLong == LuaExecuteStatusEnum.SUCCESS.getValue();
    }



    @Override
    public Boolean undoThumb(DoThumbRequestDto doThumbRequest, HttpServletRequest request) {
        validateTheParams(doThumbRequest);

        User currentUser = userService.getCurrentUser(request);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "当前用户未登录");
        }
        Long blogId = doThumbRequest.getBlogId();

        String timeSlice = getTimeSlice();

        // Redis key
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(timeSlice);//示例  thumb:temp:13:01:20
        String userThumbKey = RedisKeyUtil.getUserThumbKey(currentUser.getId()); // thumb_userId_2


        Long result = redisTemplate.execute(
                RedisLuaScriptConstant.UNTHUMB_SCRIPT,
                Arrays.asList(tempThumbKey, userThumbKey),
                currentUser.getId(),
                blogId
        );
        // 根据返回值处理结果
        if (result == LuaExecuteStatusEnum.FAIL.getValue()) {
            throw new RuntimeException("用户未点赞");
        }
        return LuaExecuteStatusEnum.SUCCESS.getValue() == result;


    }

    /**
     * 参数校验
     * @param doThumbRequestDto
     */
    private void validateTheParams(DoThumbRequestDto doThumbRequestDto) {
        if (doThumbRequestDto == null || doThumbRequestDto.getBlogId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不可为空");
        }
    }

    private String getTimeSlice() {
        DateTime nowDate = DateUtil.date();
        // 获取到当前时间前最近的整数秒，示例：  13:01:20
        return DateUtil.format(nowDate, "HH:mm:") + (DateUtil.second(nowDate) / 10) * 10;
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


//    public static void main(String[] args) {
//        String timeSlice = getTimeSlice();
//        System.out.printf(timeSlice);//13:01:20
//    }

}




