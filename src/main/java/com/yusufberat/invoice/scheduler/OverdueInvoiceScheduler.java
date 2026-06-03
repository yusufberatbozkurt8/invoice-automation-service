package com.yusufberat.invoice.scheduler;

import com.yusufberat.invoice.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueInvoiceScheduler {

    private static final Logger log = LoggerFactory.getLogger(OverdueInvoiceScheduler.class);

    private final InvoiceService invoiceService;

    public OverdueInvoiceScheduler(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Scheduled(cron = "${invoice.automation.overdue-check-cron}")
    public void checkOverdueInvoices() {
        int updated = invoiceService.markOverdueInvoices();
        if (updated > 0) {
            log.info("Vadesi geçen {} fatura OVERDUE olarak işaretlendi", updated);
        }
    }
}
