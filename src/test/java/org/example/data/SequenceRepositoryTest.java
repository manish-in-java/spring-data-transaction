package org.example.data;

import org.example.domain.Sequence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration tests for {@link SequenceRepository}
 */
@ContextConfiguration(locations = "classpath:springDataContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class SequenceRepositoryTest
{
  private static final String SEQUENCE_NAME = "Sample";

  @Autowired
  private SequenceRepository repository;

  /**
   * Sets up data required for the tests to run.
   */
  @Before
  public void setup()
  {
    repository.saveAndFlush(new Sequence(SEQUENCE_NAME));
  }

  /**
   * Tests that all available sequences can be loaded successfully.
   */
  @Test
  public void testFindAll()
  {
    final List<Sequence> sequences = repository.findAll();

    Assert.assertNotNull(sequences);
    Assert.assertNotEquals(0, sequences.size());

    for (final Sequence sequence : sequences)
    {
      Assert.assertNotNull(sequence);
      Assert.assertNotNull(sequence.getID());
      Assert.assertNotNull(sequence.getName());
      Assert.assertNotNull(sequence.getValue());
    }
  }
}
