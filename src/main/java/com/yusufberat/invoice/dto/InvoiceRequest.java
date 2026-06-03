package com.yusufberat.invoice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceRequest(
        @NotBlank
        @Size(max = 40)
        @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Sadece harf, rakam ve tire kullanılabilir")
        String invoiceNumber,
        @NotBlank @Size(max = 120) String customerName,
        @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) BigDecimal amount,
        @NotNull LocalDate issueDate,
        @NotNull LocalDate dueDate
) {
}
