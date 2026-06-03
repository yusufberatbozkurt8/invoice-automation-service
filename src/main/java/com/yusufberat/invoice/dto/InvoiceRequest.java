package com.yusufberat.invoice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceRequest(
        @NotBlank String invoiceNumber,
        @NotBlank String customerName,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull LocalDate issueDate,
        @NotNull LocalDate dueDate
) {
}
