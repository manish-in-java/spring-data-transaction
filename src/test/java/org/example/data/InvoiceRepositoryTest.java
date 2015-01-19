package org.example.data;

import org.example.domain.Invoice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@ContextConfiguration(locations = "classpath:springContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class InvoiceRepositoryTest
{
  private static final Random RANDOM = new Random();

  @Autowired
  InvoiceRepository invoiceRepository;

  @Rollback
  @Test
  public void testSaveWithError()
  {
    test();
  }

  @Test
  public void testSaveWithoutError()
  {
    test();
  }

  private void test()
  {
    final int total = RANDOM.nextInt(10) + 1;

    for (int i = 1; i <= total; ++i)
    {
      invoiceRepository.save(new Invoice(String.format("%06d", invoiceRepository.count() + 1)));
    }

    System.out.println(String.format("Added %d invoices.", total));
  }
}
