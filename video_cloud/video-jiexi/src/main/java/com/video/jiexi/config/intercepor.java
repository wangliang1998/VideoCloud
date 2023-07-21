package com.video.jiexi.config;


import com.video.jiexi.interceptor.AdminInterceptor;
import com.video.jiexi.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class intercepor implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //普通用户权限
        registry.addInterceptor(new LoginInterceptor(redisTemplate))
                .addPathPatterns("/jx/front/**");

        //管理员权限
        registry.addInterceptor(new AdminInterceptor(redisTemplate))
                .addPathPatterns("/jx/back/**");

    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
