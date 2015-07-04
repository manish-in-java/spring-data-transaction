package org.example.data;

import org.example.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Contract for data access operations on a {@link Model}.
 *
 * @param <T> The type of {@link Model}.
 */
public interface ModelRepository<T extends Model> extends JpaRepository<T, Long>
{
}
