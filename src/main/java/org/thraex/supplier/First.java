package org.thraex.supplier;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 鬼王
 * @date 2019/09/12 18:09
 */
public class First extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.print("<h1>Hello, THRAEX</h1>");
        outputStream.write("<h2>鬼王</h2>".getBytes());
    }

}
