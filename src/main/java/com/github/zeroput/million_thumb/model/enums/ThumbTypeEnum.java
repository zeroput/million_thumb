package com.github.zeroput.million_thumb.model.enums;

import lombok.Getter;

@Getter
public enum ThumbTypeEnum {

    // 点赞
    ADD_ONE(1),

    // 取消点赞
    MINUS_ONE(-1),

    // 不发生改变
    UNCHANGING(0);

    private final int value;

    ThumbTypeEnum(int value) {
        this.value = value;
    }

}
