package com.fujitsu.labs.test1

import com.nhaarman.mockitokotlin2.*
import java.io.*
import javax.servlet.RequestDispatcher
import javax.servlet.http.*
import org.junit.Assert.*

class HeServletTest {

    @org.junit.Test
    fun heServlet() {
  val stWriter = StringWriter()
  val writer = PrintWriter(stWriter)
  val request = mock<HttpServletRequest> {
  }
  val response = mock<HttpServletResponse> {
      on { getWriter() } doReturn writer
  }
  val heServlet = HelloServlet()
  heServlet.doGet(request, response)
  verify(response).getWriter()
  assertEquals("Hello, World!", stWriter.toString())
  println(stWriter.toString())
    }

    @org.junit.Test
    fun doPostTest1() {
  val dispatcher = mock<RequestDispatcher> {}
  val request = mock<HttpServletRequest> {
      on { getRequestDispatcher(any()) } doReturn dispatcher
  }
  val response = mock<HttpServletResponse> {}
  val heServlet = HelloServlet()
  heServlet.doPost(request, response)
  verify(request, times(1)).setAttribute(eq("user"), any<String>())
  verify(request, times(1)).getRequestDispatcher(any())
    }
}
