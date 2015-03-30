package org.example.service;

import org.example.data.InvoiceRepository;
import org.example.domain.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillingService
{
  @Autowired
  InvoiceNumberGenerator invoiceNumberGenerator;

  @Autowired
  InvoiceRepository invoiceRepository;

  @Retryable(maxAttempts = 40)
  public Invoice generateInvoice()
  {
    return invoiceRepository.save(new Invoice(String.format("%06d", invoiceNumberGenerator.next())));
  }
}
