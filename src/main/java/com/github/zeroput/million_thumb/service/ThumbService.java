package com.github.zeroput.million_thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zeroput.million_thumb.model.dto.thumb.DoThumbRequestDto;
import com.github.zeroput.million_thumb.model.entity.Thumb;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author Arch
* @description 针对表【thumb】的数据库操作Service
* @createDate 2025-05-09 21:42:41
*/
public interface ThumbService extends IService<Thumb> {

    Boolean doThumbAction(DoThumbRequestDto doThumbRequestDto, HttpServletRequest request);

    /**
     * 取消点赞
     * @param doThumbRequest
     * @param request
     * @return {@link Boolean }
     */
    Boolean undoThumb(DoThumbRequestDto doThumbRequest, HttpServletRequest request);
}
