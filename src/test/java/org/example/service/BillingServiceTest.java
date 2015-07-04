package org.example.service;

import org.example.domain.Invoice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link BillingService}.
 */
@ContextConfiguration(locations = { "classpath:springDataContext.xml", "classpath:springServiceContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BillingServiceTest
{
  @Autowired
  private BillingService service;

  /**
   * Tests that an invoice can be generated successfully.
   */
  @Test
  public void testGenerateInvoice()
  {
    final Invoice first = service.generateInvoice();

    Assert.assertNotNull(first);
    Assert.assertNotNull(first.getCode());
    Assert.assertNotNull(first.getID());

    final Invoice second = service.generateInvoice();

    Assert.assertNotNull(second);
    Assert.assertNotNull(second.getCode());
    Assert.assertNotNull(second.getID());

    Assert.assertNotEquals(first.getCode(), second.getCode());
    Assert.assertNotEquals(first.getID(), second.getID());
  }
}
