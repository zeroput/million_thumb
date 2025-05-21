package com.github.zeroput.million_thumb.controller;


import com.github.zeroput.million_thumb.common.BaseResponse;
import com.github.zeroput.million_thumb.common.ResultUtils;
import com.github.zeroput.million_thumb.model.dto.thumb.DoThumbRequestDto;
import com.github.zeroput.million_thumb.service.ThumbService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("thumb")
public class ThumbController {

    @Autowired
    @Qualifier("thumbDefaultService")// thumbRedisService
//    @Resource(name = "thumbRedisService") // 可以合并成一个
    private ThumbService thumbService;


    /**
     * 执行点赞
     * @param doThumbRequestDto
     * @param request
     * @return
     */
    @PostMapping("doThumb")
    public BaseResponse<Boolean> doThumb(@RequestBody DoThumbRequestDto doThumbRequestDto, HttpServletRequest request) {

        Boolean b = this.thumbService.doThumbAction(doThumbRequestDto, request);
        return ResultUtils.success(b);

    }

    /**
     * 取消点赞
     * @param doThumbRequestDto
     * @param request
     * @return
     */
    @PostMapping("cancelThumb")
    public BaseResponse<Boolean> cancelThumb(@RequestBody DoThumbRequestDto doThumbRequestDto, HttpServletRequest request) {

        Boolean b = this.thumbService.undoThumb(doThumbRequestDto, request);
        return ResultUtils.success(b);

    }


}
