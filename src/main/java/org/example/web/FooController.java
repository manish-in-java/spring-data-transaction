package org.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A sample controller that returns a non-existent view name.
 */
@Controller
public class FooController
{
  /**
   * Returns a non-existent view name.
   */
  @RequestMapping("/foo")
  public String foo()
  {
    return "foo";
  }
}
