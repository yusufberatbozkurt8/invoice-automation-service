package com.yusufberat.invoice;

import com.yusufberat.invoice.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(SecurityProperties.class)
public class InvoiceAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoiceAutomationApplication.class, args);
    }
}
