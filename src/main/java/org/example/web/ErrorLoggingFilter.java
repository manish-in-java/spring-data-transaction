package org.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Catches and logs any exceptions generated during a web request.
 */
public class ErrorLoggingFilter implements Filter
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorLoggingFilter.class);

  /**
   * {@inheritDoc}
   */
  public void destroy()
  {
  }

  /**
   * {@inheritDoc}
   */
  public void doFilter(final ServletRequest request
      , final ServletResponse response
      , final FilterChain chain)
      throws IOException, ServletException
  {
    try
    {
      chain.doFilter(request, response);
    }
    catch (final Throwable t)
    {
      LOGGER.error(t.getMessage());

      final PrintWriter writer = response.getWriter();
      writer.write("<!DOCTYPE html>"
                       + "<html>"
                       + "  <head>"
                       + "    <title>Invoice Generator</title>"
                       + "    <style>"
                       + "      body, html { background:#EEE; color:#333; font-family:Arial; font-size:12px; height:100%; margin:0; padding:0; width:100%; }"
                       + "      #container { background:#FFF; margin:0 30px; min-height:100%; padding:40px; }"
                       + "    </style>"
                       + "  </head>"
                       + "  <body>"
                       + "    <div id='container'>"
                       + "      <h3>Error!</h3>"
                       + "    </div>"
                       + "  </body>"
                       + "</html>");
      writer.flush();
      writer.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void init(final FilterConfig config) throws ServletException
  {
  }
}
