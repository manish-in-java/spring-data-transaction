package org.example.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Integration tests for {@link ErrorLoggingFilter}.
 */
public class ErrorLoggingFilterTest
{
  private ErrorLoggingFilter filter = new ErrorLoggingFilter();

  private MockMvc mock;

  /**
   * Sets up components required for the tests to run.
   */
  @Before
  public void setup() throws ServletException
  {
    mock = MockMvcBuilders.standaloneSetup(new FooController())
        .addFilters(filter)
        .build();

    filter.init(null);
  }

  /**
   * Performs clean up after tests have run.
   */
  @After
  public void teardown()
  {
    filter.destroy();
  }

  /**
   * Tests that invoking a non-existent URL results in an error that is trapped by the error logging filter.
   */
  @Test
  public void testWithError() throws Exception
  {
    mock.perform(get("/foo"));
  }
}
