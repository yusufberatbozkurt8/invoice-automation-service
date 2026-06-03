package com.yusufberat.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InvoiceAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoiceAutomationApplication.class, args);
    }
}
