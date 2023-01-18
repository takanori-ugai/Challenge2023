package com.fujitsu.labs.test1

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import java.io.PrintWriter
import java.io.StringWriter
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
