package com.github.zeroput.million_thumb;

import com.github.zeroput.million_thumb.mapper.UserMapper;
import com.github.zeroput.million_thumb.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testQuery1() {
        List<User> userWithInQuery = userMapper.getUserWithInQuery(Arrays.asList(1L, 2L));
        for (User user : userWithInQuery) {
//            System.out.println(user.getUsername());
            log.info(user.getUsername() + "====" + user.getId());

        }
    }
}
