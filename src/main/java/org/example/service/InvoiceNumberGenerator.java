package org.example.service;

import org.springframework.stereotype.Component;

@Component
public class InvoiceNumberGenerator extends SequenceGenerator
{
  String getName()
  {
    return "INV";
  }
}
