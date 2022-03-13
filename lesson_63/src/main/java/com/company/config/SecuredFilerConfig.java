package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilerConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> filterRegistrationBean() {

        FilterRegistrationBean<JwtTokenFilter> bean = new FilterRegistrationBean<JwtTokenFilter>();
        bean.setFilter(jwtTokenFilter);
        bean.addUrlPatterns("/article/*");
        bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/emailhistory/*");
        bean.addUrlPatterns("/like/create");
        bean.addUrlPatterns("/like/delate/*");
        bean.addUrlPatterns("/like/update/*");
        bean.addUrlPatterns("/region/*");
        bean.addUrlPatterns("/comment/*");
//        bean.addUrlPatterns("/comment/**");
        return bean;
    }

}
