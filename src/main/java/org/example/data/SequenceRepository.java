package org.example.data;

import org.example.domain.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceRepository extends JpaRepository<Sequence, Long>
{
  Sequence findByName(String name);
}
