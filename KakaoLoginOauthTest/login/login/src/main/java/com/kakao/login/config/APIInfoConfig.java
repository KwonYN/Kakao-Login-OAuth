package com.kakao.login.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "kakao")
public class APIInfoConfig {

    @Value("${kakao.oauth.login.redirectUrl}")
    private static String redirectUri;

    @Value("${kakao.oauth.appKey}")
    private static String appKey;

    public void printProperties() {
        System.out.println("redirectUri : " + redirectUri);
        System.out.println("appKey : " + appKey);
    }
}
