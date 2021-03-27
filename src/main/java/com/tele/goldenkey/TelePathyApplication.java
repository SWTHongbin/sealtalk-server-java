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
import software.amazon.awssdk.core.SdkSystemSetting;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@Import(SpringContextUtil.class)
@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan("com.tele.goldenkey.dao")
public class TelePathyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        SpringApplication.run(TelePathyApplication.class, args);
    }

    //为了打包springboot项目
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
