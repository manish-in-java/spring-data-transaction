package org.example.data;

import org.example.domain.Sequence;

/**
 * Contract for data access operations on {@link Sequence}.
 */
public interface SequenceRepository extends ModelRepository<Sequence>
{
  /**
   * Finds a sequence with a specified name.
   *
   * @param name The sequence name to find.
   * @return A {@link Sequence} if one with the specified name is found,
   * {@code null} otherwise.
   */
  Sequence findByName(String name);
}
