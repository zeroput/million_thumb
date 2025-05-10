package com.github.zeroput.million_thumb.common;

import com.github.zeroput.million_thumb.constant.ThumbConstant;

/**
 * copy过来的
 */
public class RedisKeyUtil {

    public static String getUserThumbKey(Long userId) {

        return ThumbConstant.USER_THUMB_KEY_PREFIX + userId;
    }

}