package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Represents an invoice for a transaction of goods or services.
 */
@Entity
@Table(name = "invoice")
public class Invoice extends Model
{
  @Column(name = "code", unique = true, updatable = false)
  @NotNull
  private final String code;

  /**
   * Deliberately hidden to prevent direct instantiation.
   */
  Invoice()
  {
    this(null);
  }

  /**
   * Creates an invoice with a specified unique code.
   *
   * @param code The unique invoice code.
   */
  public Invoice(final String code)
  {
    this.code = code;
  }

  /**
   * Gets the unique invoice code.
   *
   * @return The unique invoice code.
   */
  public String getCode()
  {
    return code;
  }
}
