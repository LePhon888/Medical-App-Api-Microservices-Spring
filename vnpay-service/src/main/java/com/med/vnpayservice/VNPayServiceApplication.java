package com.med.vnpayservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
public class VNPayServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(VNPayServiceApplication.class, args);
    }
}