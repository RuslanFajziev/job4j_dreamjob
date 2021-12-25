package ru.job4j.dream.servlet;

import ru.job4j.dream.store.Store;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IdFotoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var status = req.getParameter("del");
        var idFile = req.getParameter("name");
        if (status.equals("true")) {
            File file = new File("d:\\test\\images\\" + idFile + ".png");
            file.delete();
            Store.instOf().delCandidate(Integer.parseInt(idFile));
            resp.sendRedirect(req.getContextPath() + "/candidates.do");
        } else {
            req.setAttribute("name", idFile);
            req.getRequestDispatcher("/PhotoUpload.jsp").forward(req, resp);
        }
    }
}
