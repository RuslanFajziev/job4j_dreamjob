package ru.job4j.dream.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PostCandidateJsonServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    private class JsonClass {
        List<Post> postList;
        List<Candidate> candidatestList;

        public JsonClass(List<Post> postList, List<Candidate> candidatestList) {
            this.postList = postList;
            this.candidatestList = candidatestList;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream outputStream = resp.getOutputStream();
        List<Post> postList = DbStore.instOf().findAllPosts(true);
        List<Candidate> candidatestList = DbStore.instOf().findAllCandidates(true);
        JsonClass jsonClass = new JsonClass(postList, candidatestList);
        String json = GSON.toJson(jsonClass);
        outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}