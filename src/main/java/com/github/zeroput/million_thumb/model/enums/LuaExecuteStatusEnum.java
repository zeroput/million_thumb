package com.github.zeroput.million_thumb.model.enums;

import lombok.Getter;

@Getter
public enum LuaExecuteStatusEnum {
    // 成功  
    SUCCESS(1L),  
    // 失败  
    FAIL(-1L),  
    ;  
  
    private final Long value;

    LuaExecuteStatusEnum(Long value) {
        this.value = value;  
    }  
}
