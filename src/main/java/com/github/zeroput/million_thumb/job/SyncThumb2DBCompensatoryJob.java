package com.github.zeroput.million_thumb.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.github.zeroput.million_thumb.common.RedisKeyUtil;
import com.github.zeroput.million_thumb.constant.ThumbConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class SyncThumb2DBCompensatoryJob {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SyncThumb2DBJob syncThumb2DBJob;


    @Scheduled(cron = "0 0 2 * * *")
//    @Scheduled(cron = "0 * * * * *") 测试用
    @Transactional(rollbackFor = Exception.class)
    public void run() {

        log.info("开始补偿数据");

        Set<String> thumbKeys = redisTemplate.keys(RedisKeyUtil.getTempThumbKey("") + "*");

        HashSet<String> needHandleDataSet = new HashSet<>();

        for (String key : thumbKeys) {
            if (ObjUtil.isNotNull(key)) {
//                thumb:temp:16:23:50 去除掉thumb:temp:  只保留日期16:23:50
                needHandleDataSet.add(key.replace(ThumbConstant.TEMP_THUMB_KEY_PREFIX.formatted(""), ""));
            }
        }

        if (CollUtil.isEmpty(needHandleDataSet)) {
            log.info("没有要补偿的数据，已退出job");
            return;
        }

        for (String dateStr : needHandleDataSet) {
            syncThumb2DBJob.syncThumb2DBByDate(dateStr);
        }

        log.info("补偿数据同步已完成");// 针对那些没有及时被10秒定时任务处理的key，如果长期不处理 旧会一直存在

    }
}
