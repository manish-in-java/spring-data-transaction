package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sequence")
public class Sequence extends Relation
{
  @Column(unique = true, updatable = false)
  @NotNull
  private String name;

  @Column
  @NotNull
  private Long value = 0L;

  Sequence()
  {
  }

  public Sequence(String name)
  {
    this.name = name;
    this.value = 1L;
  }

  public String getName()
  {
    return name;
  }

  public Long getValue()
  {
    return value++;
  }
}
