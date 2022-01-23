package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore {

    private static final MemStore INST = new MemStore();
    private static final AtomicInteger POST_ID = new AtomicInteger(3);
    private static final AtomicInteger CAND_ID = new AtomicInteger(3);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(2, new Post(2, "Vasa", "Middle Java Job", new Date()));
        posts.put(3, new Post(3, "Ruslan", "Senior Java Job", new Date()));
        candidates.put(1, new Candidate(1, "Junior Java", "Rostov", new Date()));
        candidates.put(2, new Candidate(2, "Middle Java", "Krasnodar", new Date()));
        candidates.put(3, new Candidate(3, "Senior Java", "Moscow", new Date()));
    }

    public static MemStore instOf() {
        return INST;
    }

    public void deleteCandidate(int id) {
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

    public Candidate findByIdCandidate(int id) {
        return candidates.get(id);
    }

    public void saveCandidate(Candidate cand) {
        if (cand.getId() == 0) {
            cand.setId(CAND_ID.incrementAndGet());
        }
        candidates.put(cand.getId(), cand);
    }
}