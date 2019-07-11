package org.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class InsuranceScheduleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceScheduleServiceApplication.class, args);
    }

}
