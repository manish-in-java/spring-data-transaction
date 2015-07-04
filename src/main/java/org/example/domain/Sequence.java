package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Represents an arbitrary sequence of numbers.
 */
@Entity
@Table(name = "sequence")
public class Sequence extends Model
{
  @Column(unique = true, updatable = false)
  @NotNull
  private String name;

  @Column
  @NotNull
  private Long value = 0L;

  /**
   * Deliberately hidden to prevent direct instantiation.
   */
  Sequence()
  {
  }

  /**
   * Creates a sequence with a specified name.
   *
   * @param name The sequence name.
   */
  public Sequence(final String name)
  {
    this.name = name;
    this.value = 0L;
  }

  /**
   * Gets the sequence name.
   *
   * @return The sequence name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Gets the current sequence value.
   *
   * @return The current sequence value.
   */
  public Long getValue()
  {
    return ++value;
  }
}
