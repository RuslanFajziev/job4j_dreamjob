package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.Candidate;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {

    private static final Store INST = new Store();
    private static final AtomicInteger POST_ID = new AtomicInteger(3);
    private static final AtomicInteger CAND_ID = new AtomicInteger(3);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private static final Properties PROPERTIES = loadProperties();

    private Store() {
        posts.put(1, new Post(1, "Petr", "Junior Java Job", "2021-12-14"));
        posts.put(2, new Post(2, "Vasa", "Middle Java Job", "2019-10-28"));
        posts.put(3, new Post(3, "Ruslan", "Senior Java Job", "2022-06-01"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    private static Properties loadProperties() {
        var properties = new Properties();
        try (InputStream in = Store.class.getClassLoader().getResourceAsStream("Application.properties")) {
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return properties;
    }

    public String getProperty(String param) {
        return PROPERTIES.getProperty(param);
    }

    public void delCandidate(int id) {
        candidates.remove(id);
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public Candidate findByIdCand(int id) {
        return candidates.get(id);
    }

    public void saveCand(Candidate cand) {
        if (cand.getId() == 0) {
            cand.setId(CAND_ID.incrementAndGet());
        }
        candidates.put(cand.getId(), cand);
    }
}