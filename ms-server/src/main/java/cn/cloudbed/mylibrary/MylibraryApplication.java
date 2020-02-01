package cn.cloudbed.mylibrary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.cloudbed.mylibrary.*") // 扫描Mybatis的mapper文件
public class MylibraryApplication {

    public static void main(String[] args) {

        SpringApplication.run(MylibraryApplication.class, args);

    }

}
