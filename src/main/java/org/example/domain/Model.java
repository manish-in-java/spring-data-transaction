package org.example.domain;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Represents a domain entity.
 */
@MappedSuperclass
public abstract class Model
{
  @Generated(GenerationTime.INSERT)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long id;

  /**
   * Gets the unique identifier for this entity.
   *
   * @return The unique identifier for this entity.
   */
  public Long getID()
  {
    return id;
  }
}
