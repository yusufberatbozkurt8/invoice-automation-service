package com.yusufberat.invoice.config;

import com.yusufberat.invoice.model.Invoice;
import com.yusufberat.invoice.model.InvoiceStatus;
import com.yusufberat.invoice.repository.InvoiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seedInvoices(InvoiceRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.save(sample("INV-2026-001", "ABC Ltd.", "15000.00", 30, InvoiceStatus.SENT));
            repository.save(sample("INV-2026-002", "XYZ A.Ş.", "8200.50", -5, InvoiceStatus.SENT));
            repository.save(sample("INV-2026-003", "Demo Müşteri", "1200.00", 15, InvoiceStatus.DRAFT));
        };
    }

    private Invoice sample(String number, String customer, String amount, int dueDaysOffset, InvoiceStatus status) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(number);
        invoice.setCustomerName(customer);
        invoice.setAmount(new BigDecimal(amount));
        invoice.setIssueDate(LocalDate.now().minusDays(10));
        invoice.setDueDate(LocalDate.now().plusDays(dueDaysOffset));
        invoice.setStatus(status);
        return invoice;
    }
}
