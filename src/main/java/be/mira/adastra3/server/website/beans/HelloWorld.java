package be.mira.adastra3.server.website.beans;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class HelloWorld extends HttpServlet {
  public void doGet (HttpServletRequest req,
                     HttpServletResponse res)
    throws ServletException, IOException
  {
    PrintWriter out = res.getWriter();

    out.println("Hello, world!");
    out.close();
  }
}
