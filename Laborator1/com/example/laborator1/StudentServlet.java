package com.example.laborator1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private static final List<String> students = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(students.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        else
        {
//            PrintWriter out = response.getWriter();
//            students.stream().forEach(s -> out.println(s));

            String format = request.getHeader("Accept");
            if(format == null || format.isEmpty() || "*/*".equals(format)){
                format = "text/plain";
            }
            final List<String> formats = Arrays.asList("text/plain", "text/html", "application/json", "application/xml");
            if(!formats.contains(format)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                response.setHeader("Content-type", format);
                PrintWriter out = response.getWriter();

                switch (format) {
                    case "text/plain":
                        students.stream().forEach(s -> out.println(s));
                        break;
                    case "text/html":
                        out.println("<ul>");
                        students.stream().forEach(s -> out.println("<li>" + s + "</li>"));
                        out.println("</ul>");
                        break;
                    case "application/json":
                        out.print("[");
                        for(int i = 0; i< students.size(); i++) {
                            if (i > 0) {
                                out.print(", ");
                            }
                            out.print("\"" + students.get(i) + "\"");
                        }
                        out.println("]");
                        break;
                    case "application/xml":
                        out.println("<students>");
                        students.stream().forEach(s -> out.println("<student>" + s + "</student>"));
                        out.println("</students>");
                        break;
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");

        if(name == null || name.isEmpty() || students.contains(name)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            students.add(name);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String name = request.getParameter("name");
      if(name == null || name.isEmpty()){
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      } else if(!students.contains(name)) {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      } else {
          response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        if(name == null || name.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if(!students.contains(name)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            students.remove(name);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
