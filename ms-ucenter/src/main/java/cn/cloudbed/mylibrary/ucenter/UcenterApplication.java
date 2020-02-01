package cn.cloudbed.mylibrary.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient //打开euraka客户端
@EnableFeignClients //打开feign(以及ribbon)客户端
@MapperScan("cn.cloudbed.mylibrary.ucenter.dao")
@EntityScan("cn.cloudbed.mylibrary.framework.domain.ucenter")//扫描实体类
@ComponentScan(basePackages={"cn.cloudbed.mylibrary.api"})//扫描接口
@ComponentScan(basePackages={"cn.cloudbed.mylibrary.framework"})//扫描common下的所有类
@SpringBootApplication
public class UcenterApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(UcenterApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}