package ru.job4j.dream.servlet;

import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        var dbStore = DbStore.instOf();
        var rsl = dbStore.regUser(name, email, password);
        if (rsl != -1) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", dbStore.findUserForId(rsl));
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            req.setAttribute("error", "Уже есть пользователь с email: " + email);
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
    }
}