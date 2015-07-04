package org.example.service;

import org.example.data.InvoiceRepository;
import org.example.domain.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic operations related to billing.
 */
@Service
@Transactional
public class BillingService
{
  @Autowired
  private InvoiceCodeGenerator invoiceCodeGenerator;

  @Autowired
  private InvoiceRepository invoiceRepository;

  /**
   * Generates an invoice.
   *
   * @return The generated {@link Invoice}.
   */
  @Retryable(backoff = @Backoff(delay = 2000, multiplier = 2, random = true), maxAttempts = 40)
  public Invoice generateInvoice()
  {
    return invoiceRepository.save(new Invoice(String.format("%06d", invoiceCodeGenerator.next())));
  }
}
