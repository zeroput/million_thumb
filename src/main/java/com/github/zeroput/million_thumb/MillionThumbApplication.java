package com.github.zeroput.million_thumb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
//@EnableScheduling
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
public class MillionThumbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MillionThumbApplication.class, args);
	}

}
