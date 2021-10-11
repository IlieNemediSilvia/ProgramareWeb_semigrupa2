package com.example.laborator1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/")
public class CountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.getWriter().println("<p> The context count is: " + getContextCounter(request) + "</p>");
        response.getWriter().println("<p> The session count is: " + getSessionCounter(request) + "</p>");
    }
    private int getContextCounter(HttpServletRequest request){
        Object counter = request.getServletContext().getAttribute("counter");
        int count;
        if(counter != null) {
            count = ((AtomicInteger)counter).incrementAndGet();
        } else {
            request.getServletContext().setAttribute("counter", new AtomicInteger(1));
            count = 1;
        }
        return count;
    }
    private int getSessionCounter(HttpServletRequest request){
        Object counter = request.getSession().getAttribute("counter");
        int count;
        if(counter != null) {
            count = ((AtomicInteger)counter).incrementAndGet();
        } else {
            request.getSession().setAttribute("counter", new AtomicInteger(1));
            count = 1;
        }
        return count;
    }
}
