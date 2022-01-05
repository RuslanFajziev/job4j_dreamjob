package ru.job4j.dream.servlet;

import org.junit.Test;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServletsTests {
    @Test
    public void whenCreatePost() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        var name = "test name";
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn(name);
        when(req.getParameter("description")).thenReturn("d");
        when(req.getParameter("created")).thenReturn("03.12.2020");
        new PostServlet().doPost(req, resp);
        Post post = DbStore.instOf().findByName(name);
        assertEquals(post.getName(), name);
    }
}