package ru.job4j.dream.servlet;

import org.junit.*;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.DbStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CandidateServletTest {
    public static Store store;

    @BeforeClass
    public static void initConnection() {
        store = DbStore.instOf();
    }

    @After
    public void wipeTable() {
        store.wipeTableCandidate();
    }

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Petr");
        new CandidateServlet().doPost(req, resp);
        Candidate candidate = DbStore.instOf().findByNameCandidate("Petr");
        assertEquals(candidate.getName(), "Petr");
    }
}