package com.fujitsu.labs.test1

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/** Sample Servlet.  */
@WebServlet(name = "HelloServlet", urlPatterns = ["hello"], loadOnStartup = 1)
class HelloServlet : HttpServlet() {
//    @Throws(ServletException::class, IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.writer.print("Hello, World!")
    }

//    @Throws(ServletException::class, IOException::class)
    public override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        var name: String? = request.getParameter("name")
        if (name == null) name = "World"
        request.setAttribute("user", name)
        request.getRequestDispatcher("response.jsp").forward(request, response)
    }
}
