package ru.job4j.dream.servlet;

import org.junit.*;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.DbStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostServletTest {
    public static Store store;

    @BeforeClass
    public static void initConnection() {
        store = DbStore.instOf();
    }

    @After
    public void wipeTable() {
        store.wipeTablePost();
    }

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Petr");
        when(req.getParameter("description")).thenReturn("Junior Java Job");
        when(req.getParameter("created")).thenReturn("2021-12-14");
        new PostServlet().doPost(req, resp);
        Post post = DbStore.instOf().findByNamePost("Petr");
        assertEquals(post.getName(), "Petr");
    }
}