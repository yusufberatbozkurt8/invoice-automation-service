package com.yusufberat.invoice.repository;

import com.yusufberat.invoice.model.Invoice;
import com.yusufberat.invoice.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByStatus(InvoiceStatus status);

    @Query("""
            SELECT i FROM Invoice i
            WHERE i.status = com.yusufberat.invoice.model.InvoiceStatus.SENT
              AND i.dueDate < :today
            """)
    List<Invoice> findSentAndPastDue(@Param("today") LocalDate today);
}
