package com.dooffe.dfepay.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tars
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World ! App!");
        SpringApplication.run(Application.class, args);
    }
}