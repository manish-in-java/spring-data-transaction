package org.example.service;

import org.example.data.SequenceRepository;
import org.example.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Responsible for generating sequence numbers of a specific type.
 */
public abstract class SequenceGenerator
{
  @Autowired
  protected SequenceRepository sequenceRepository;

  /**
   * Gets the next sequence number of a specific type.
   *
   * @return The next sequence number of a specific type.
   */
  @Transactional
  public Long next()
  {
    // Attempt to load a sequence of a specified name.
    Sequence sequence = sequenceRepository.findByName(getName());
    if (sequence == null)
    {
      // If not found, create a new one.
      sequence = new Sequence(getName());
    }

    final Long value = sequence.getValue();
    sequenceRepository.saveAndFlush(sequence);

    return value;
  }

  /**
   * Gets the name of the sequence for which numbers need
   * to be generated.
   *
   * @return The name of the sequence for which numbers
   * need to be generated.
   */
  abstract String getName();
}
