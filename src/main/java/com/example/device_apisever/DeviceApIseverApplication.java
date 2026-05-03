package com.example.device_apisever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DeviceApIseverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceApIseverApplication.class, args);
    }

}

