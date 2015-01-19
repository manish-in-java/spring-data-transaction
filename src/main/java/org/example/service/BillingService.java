package org.example.service;

import org.example.data.InvoiceRepository;
import org.example.domain.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class BillingService
{
  private static final Random RANDOM = new Random();

  @Autowired
  InvoiceRepository invoiceRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Invoice generateInvoice()
  {
    final Invoice invoice = invoiceRepository.save(new Invoice(String.format("%06d", invoiceRepository.count() + 1)));

    if (RANDOM.nextInt(101) % 5 == 0)
    {
      throw new RuntimeException();
    }

    return invoice;
  }
}
