package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    Post findById(int id);

    void saveCandidate(Candidate candidate);

    Candidate findByIdCandidate(int id);

    void delete(int id);

    void deleteCandidate(int id);

    void regUser(String name, String email, String password);

}