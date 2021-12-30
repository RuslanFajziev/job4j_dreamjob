package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import static org.junit.Assert.*;

public class DbStoreTest {
    @Test
    public void whenCreatePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Petr", "Junior Java Job", "2021-12-14");
        store.save(post);
        Post postInDb = store.findById(post.getId());
        assertEquals(postInDb.getName(), post.getName());
    }

    @Test
    public void whenUpdatePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Petr", "Junior Java Job", "2021-12-14");
        store.save(post);
        Post postInDb = store.findById(post.getId());
        Post postNew = new Post(post.getId(), "Vasia", "Junior Java Job", "2021-12-14");
        assertEquals(postInDb.getName(), post.getName());
        store.save(postNew);
        assertEquals(postNew.getName(), "Vasia");
    }

    @Test
    public void whenDeletePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Petr", "Junior Java Job", "2021-12-14");
        store.save(post);
        var id = post.getId();
        store.delete(id);
        assertNull(store.findById(id));
    }

    @Test
    public void whenCreateCandidate() {
        Store store = DbStore.instOf();
        Candidate candidates = new Candidate(0, "Junior Java");
        store.saveCandidate(candidates);
        Candidate candidatesInDb = store.findByIdCandidate(candidates.getId());
        assertEquals(candidatesInDb.getName(), candidates.getName());
    }

    @Test
    public void whenUpdateCandidate() {
        Store store = DbStore.instOf();
        Candidate candidates = new Candidate(0, "Junior Java");
        store.saveCandidate(candidates);
        Candidate candidatesInDb = store.findByIdCandidate(candidates.getId());
        Candidate candidatesNew = new Candidate(candidates.getId(), "Middle Java");
        assertEquals(candidatesInDb.getName(), candidates.getName());
        store.saveCandidate(candidatesNew);
        assertEquals(candidatesNew.getName(), "Middle Java");
    }

    @Test
    public void whenDeleteCandidate() {
        Store store = DbStore.instOf();
        Candidate candidates = new Candidate(0, "Junior Java");
        store.saveCandidate(candidates);
        var id = candidates.getId();
        store.deleteCandidate(id);
        assertNull(store.findByIdCandidate(id));
    }
}