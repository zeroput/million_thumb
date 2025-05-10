package com.github.zeroput.million_thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zeroput.million_thumb.mapper.BlogMapper;
import com.github.zeroput.million_thumb.model.entity.Blog;
import com.github.zeroput.million_thumb.service.BlogService;
import org.springframework.stereotype.Service;

/**
* @author Arch
* @description 针对表【blog】的数据库操作Service实现
* @createDate 2025-05-09 21:42:41
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

}




