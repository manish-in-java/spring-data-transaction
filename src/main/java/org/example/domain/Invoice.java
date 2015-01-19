package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "invoice")
public class Invoice extends Relation
{
  @Column(name = "code", unique = true, updatable = false)
  @NotNull
  private final String code;

  Invoice()
  {
    this(null);
  }

  public Invoice(String code)
  {
    this.code = code;
  }

  public String getCode()
  {
    return code;
  }
}
