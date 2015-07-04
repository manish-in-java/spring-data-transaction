package org.example.service;

import org.springframework.stereotype.Component;

/**
 * Generates invoice codes as sequential numbers.
 */
@Component
public class InvoiceCodeGenerator extends SequenceGenerator
{
  /**
   * Gets the name of the sequence to use for generating
   * invoice codes.
   *
   * @return The name of the sequence to use for generating
   * invoice codes.
   */
  String getName()
  {
    return "INV";
  }
}
