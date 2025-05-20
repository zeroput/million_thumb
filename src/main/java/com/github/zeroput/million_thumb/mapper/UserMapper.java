package com.github.zeroput.million_thumb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zeroput.million_thumb.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Arch
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-05-09 21:42:41
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 使用In来查询
     * @param userIdList
     * @return
     */
    List<User> getUserWithInQuery(@Param("userIdList") List<Long> userIdList);

}




