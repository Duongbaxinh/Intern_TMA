package com.seatmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication

public class SeatmanageApplication {

    public static void main(String[] args) {
              SpringApplication.run(SeatmanageApplication.class, args);
    }

}
