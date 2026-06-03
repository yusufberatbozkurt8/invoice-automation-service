package com.yusufberat.invoice.controller;

import com.yusufberat.invoice.dto.InvoiceRequest;
import com.yusufberat.invoice.dto.InvoiceResponse;
import com.yusufberat.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse create(@Valid @RequestBody InvoiceRequest request) {
        return invoiceService.createDraft(request);
    }

    @GetMapping
    public List<InvoiceResponse> list() {
        return invoiceService.listAll();
    }

    @GetMapping("/{id}")
    public InvoiceResponse get(@PathVariable Long id) {
        return invoiceService.getById(id);
    }

    @PostMapping("/{id}/send")
    public InvoiceResponse send(@PathVariable Long id) {
        return invoiceService.send(id);
    }

    @PostMapping("/{id}/pay")
    public InvoiceResponse pay(@PathVariable Long id) {
        return invoiceService.markPaid(id);
    }

    @PostMapping("/{id}/cancel")
    public InvoiceResponse cancel(@PathVariable Long id) {
        return invoiceService.cancel(id);
    }

    @PostMapping("/automation/overdue-check")
    public Map<String, Integer> runOverdueCheck() {
        int updated = invoiceService.markOverdueInvoices();
        return Map.of("updatedCount", updated);
    }
}
