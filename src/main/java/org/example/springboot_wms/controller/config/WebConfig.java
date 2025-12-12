package org.example.springboot_wms.controller.config;

import org.example.springboot_wms.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    // 读取你在 application.yml 里配置的上传路径
    // 如果你没配，这里请手动写死你的图片保存文件夹，比如 "D:/avatar_upload/"
    // 注意：必须要以 / 结尾
    @Value("${upload.avatar.path}")
    private String uploadPath;

    @Value("${upload.avatar.url}")
    private String uploadUrl; // 比如 /uploads/avatar/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 关键配置：把 URL 请求映射到本地文件
        // 比如请求 http://localhost:8080/uploads/avatar/1.jpg
        // 就会去 D:/avatar_upload/1.jpg 找
        registry.addResourceHandler(uploadUrl + "**")
                .addResourceLocations("file:" + uploadPath);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor()) // 添加我们写的拦截器
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns( // 排除不需要拦截的请求
                        "/user/login", // 登录接口必须放行，否则无法登录
                        "/user/hello", // 测试接口
                        "/uploads/**", // 头像等静态资源
                        "/user/test" // 你的测试接口
                );
    }
}