package com.tele.goldenkey;

import com.tele.goldenkey.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@Import(SpringContextUtil.class)
@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan("com.tele.goldenkey.dao")
public class SealtalkServerApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SealtalkServerApplication.class, args);
    }

    //为了打包springboot项目
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
