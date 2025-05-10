package com.github.zeroput.million_thumb.model.dto.thumb;

import lombok.Data;

@Data
public class DoThumbRequestDto {
    /**
     * 待点赞的博客id
     * 用户在session中取
     */
    private Long blogId;
}
