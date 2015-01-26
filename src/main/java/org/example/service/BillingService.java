package org.example.service;

import org.example.data.InvoiceRepository;
import org.example.domain.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService
{
  @Autowired
  InvoiceNumberGenerator invoiceNumberGenerator;

  @Autowired
  InvoiceRepository invoiceRepository;

  @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
  public Invoice generateInvoice()
  {
    return invoiceRepository.save(new Invoice(String.format("%06d", invoiceNumberGenerator.next())));
  }
}
