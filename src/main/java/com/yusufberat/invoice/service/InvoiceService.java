package com.yusufberat.invoice.service;

import com.yusufberat.invoice.dto.InvoiceRequest;
import com.yusufberat.invoice.dto.InvoiceResponse;
import com.yusufberat.invoice.model.Invoice;
import com.yusufberat.invoice.model.InvoiceStatus;
import com.yusufberat.invoice.repository.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceResponse createDraft(InvoiceRequest request) {
        validateDates(request);
        invoiceRepository.findByInvoiceNumber(request.invoiceNumber()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Fatura numarası zaten kayıtlı");
        });

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(request.invoiceNumber());
        invoice.setCustomerName(request.customerName());
        invoice.setAmount(request.amount());
        invoice.setIssueDate(request.issueDate());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(InvoiceStatus.DRAFT);
        return InvoiceResponse.from(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> listAll() {
        return invoiceRepository.findAll().stream().map(InvoiceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long id) {
        return InvoiceResponse.from(findOrThrow(id));
    }

    public InvoiceResponse send(Long id) {
        Invoice invoice = findOrThrow(id);
        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sadece taslak faturalar gönderilebilir");
        }
        invoice.setStatus(InvoiceStatus.SENT);
        return InvoiceResponse.from(invoice);
    }

    public InvoiceResponse markPaid(Long id) {
        Invoice invoice = findOrThrow(id);
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "İptal edilmiş fatura ödenemez");
        }
        invoice.setStatus(InvoiceStatus.PAID);
        return InvoiceResponse.from(invoice);
    }

    public InvoiceResponse cancel(Long id) {
        Invoice invoice = findOrThrow(id);
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ödenmiş fatura iptal edilemez");
        }
        invoice.setStatus(InvoiceStatus.CANCELLED);
        return InvoiceResponse.from(invoice);
    }

    public int markOverdueInvoices() {
        List<Invoice> overdueCandidates = invoiceRepository.findSentAndPastDue(java.time.LocalDate.now());
        overdueCandidates.forEach(invoice -> invoice.setStatus(InvoiceStatus.OVERDUE));
        return overdueCandidates.size();
    }

    private Invoice findOrThrow(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatura bulunamadı: " + id));
    }

    private void validateDates(InvoiceRequest request) {
        if (request.dueDate().isBefore(request.issueDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vade tarihi düzenleme tarihinden önce olamaz");
        }
    }
}
