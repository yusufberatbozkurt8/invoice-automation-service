package com.yusufberat.invoice;

import com.yusufberat.invoice.dto.InvoiceRequest;
import com.yusufberat.invoice.dto.InvoiceResponse;
import com.yusufberat.invoice.model.InvoiceStatus;
import com.yusufberat.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    void draftToSentToPaidFlow() {
        InvoiceResponse created = invoiceService.createDraft(new InvoiceRequest(
                "INV-TEST-99",
                "Test Müşteri",
                new BigDecimal("500.00"),
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        ));
        assertThat(created.status()).isEqualTo(InvoiceStatus.DRAFT);

        InvoiceResponse sent = invoiceService.send(created.id());
        assertThat(sent.status()).isEqualTo(InvoiceStatus.SENT);

        InvoiceResponse paid = invoiceService.markPaid(created.id());
        assertThat(paid.status()).isEqualTo(InvoiceStatus.PAID);
    }
}
