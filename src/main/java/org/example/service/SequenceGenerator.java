package org.example.service;

import org.example.data.SequenceRepository;
import org.example.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class SequenceGenerator
{
  @Autowired
  protected SequenceRepository sequenceRepository;

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Long next()
  {
    Sequence sequence = sequenceRepository.findByName(getName());
    if (sequence == null)
    {
      sequence = new Sequence(getName());
    }

    final Long value = sequence.getValue();
    sequenceRepository.save(sequence);

    return value;
  }

  abstract String getName();
}
