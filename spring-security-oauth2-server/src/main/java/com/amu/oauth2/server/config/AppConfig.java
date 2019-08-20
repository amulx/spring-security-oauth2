package com.amu.oauth2.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;

/**
 * 考虑到这个 session 在验证码过滤器中还得使用，所以自定义了一个配置，直接注入到了spring中
 * 这样，在 Controller 层直接通过 @Autowired 引用即可。
 */
@Configuration
public class AppConfig {

    @Bean("sessionStrategy")
    public SessionStrategy registBean(){
        SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        return sessionStrategy;
    }
}
