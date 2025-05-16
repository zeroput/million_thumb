package com.github.zeroput.million_thumb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zeroput.million_thumb.model.entity.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author Arch
* @description 针对表【blog】的数据库操作Mapper
* @createDate 2025-05-09 21:42:41
* @Entity generator.domain.Blog
*/
public interface BlogMapper extends BaseMapper<Blog> {

    // @@Param("countMap")
    void batchUpdateBlogThumbCount(@Param("countMap") Map<Long, Long> countMap);

}




