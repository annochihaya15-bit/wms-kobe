package org.example.springboot_wms.common;

import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.annotation.MapperScan;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.context.annotation.Bean;

@Configuration // 告诉Spring这是配置类
@MapperScan("org.example.springboot_wms.mapper") // 扫描你的Mapper接口包
public class MybatisPlusConfig {

    public MybatisPlusConfig() {
        System.out.println("===== MybatisPlusConfig 配置类被加载了 =====");
    }
    @Bean // 把分页拦截器注册为Spring Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页拦截器，指定数据库类型为MySQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
