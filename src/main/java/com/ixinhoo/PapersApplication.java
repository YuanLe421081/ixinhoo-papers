package com.ixinhoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * 修改war包启动方式
 */
//@ComponentScan(basePackages = "com.ixinhoo,com.chunecai")
@SpringBootApplication(scanBasePackages = {"com.ixinhoo,com.chunecai"})
// 开启缓存，需要显示的指定 TODO cici 暂时注释掉，测试
//@EnableCaching
@ServletComponentScan // 扫描使用注解方式的servlet
@EnableAutoConfiguration
public class PapersApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PapersApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PapersApplication.class, args);
    }

}
