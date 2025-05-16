package com.github.zeroput.million_thumb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling // 开启定时任务 否则job不运行
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
public class MillionThumbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MillionThumbApplication.class, args);
	}

}
