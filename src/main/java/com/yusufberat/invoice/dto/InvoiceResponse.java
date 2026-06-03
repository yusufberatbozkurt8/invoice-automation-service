package com.yusufberat.invoice.dto;

import com.yusufberat.invoice.model.Invoice;
import com.yusufberat.invoice.model.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceResponse(
        Long id,
        String invoiceNumber,
        String customerName,
        BigDecimal amount,
        LocalDate issueDate,
        LocalDate dueDate,
        InvoiceStatus status
) {
    public static InvoiceResponse from(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getCustomerName(),
                invoice.getAmount(),
                invoice.getIssueDate(),
                invoice.getDueDate(),
                invoice.getStatus()
        );
    }
}
