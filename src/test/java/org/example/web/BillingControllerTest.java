package org.example.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link BillingController}.
 */
@ContextConfiguration(locations = { "classpath:springDataContext.xml", "classpath:springServiceContext.xml", "classpath:springWebContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BillingControllerTest
{
  @Autowired
  private BillingController controller;

  private MockMvc mock;

  /**
   * Sets up components required for the tests to run.
   */
  @Before
  public void setup()
  {
    mock = MockMvcBuilders.standaloneSetup(controller).build();
  }

  /**
   * Tests that an invoice can be generated successfully.
   */
  @Test
  public void testInvoice() throws Exception
  {
    mock.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html"));
  }
}
