package com.github.zeroput.million_thumb.common;

import cn.hutool.core.date.DateTime;
import com.github.zeroput.million_thumb.constant.ThumbConstant;

/**
 * copy过来的
 */
public class RedisKeyUtil {

    public static String getUserThumbKey(Long userId) {

        return ThumbConstant.USER_THUMB_KEY_PREFIX + userId;
    }

    /**
     * 格式化时间戳
     */
    public static String getTempThumbKey(String timeStr) {
        return ThumbConstant.TEMP_THUMB_KEY_PREFIX.formatted(timeStr);
    }

//    /**
//     * 测试一下时间戳
//     * @param args
//     */
//    public static void main(String[] args) {
//        System.out.println(getTempThumbKey(new DateTime().toString()));
//    }

}