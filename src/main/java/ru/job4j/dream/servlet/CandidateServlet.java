package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.MemStoreStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", MemStoreStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        MemStoreStore.instOf().saveCand(new Candidate(Integer.valueOf(req.getParameter("id")), req.getParameter("name")));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}