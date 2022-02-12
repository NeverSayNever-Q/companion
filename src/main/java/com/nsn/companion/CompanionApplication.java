package com.nsn.companion;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.nsn.companion.mapper")
@SpringBootApplication
public class CompanionApplication {
    private static final Logger LOG = LoggerFactory.getLogger(CompanionApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CompanionApplication.class, args);
        LOG.info("启动成功！！");
    }

}
