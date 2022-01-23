package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.List;

public interface Store {
    List<Post> findAllPosts(Boolean now);

    List<Candidate> findAllCandidates(Boolean today);

    List<City> findAllCities();

    void save(Post post);

    Post findById(int id);

    void saveCandidate(Candidate candidate);

    void saveCity(City city);

    Candidate findByIdCandidate(int id);

    City findByIdCity(int id);

    void delete(int id);

    void deleteCandidate(int id);

    void wipeTablePost();

    void wipeTableCandidate();

    int regUser(String name, String email, String password);

    Post findByNamePost(String name);

    Candidate findByNameCandidate(String name);

    User findUserForEmail(String email);

    User findUserForId(int id);
}