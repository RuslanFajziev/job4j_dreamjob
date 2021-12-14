package ru.jobj.dream.store;

import ru.jobj.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Petr", "Junior Java Job", "2021-12-14"));
        posts.put(2, new Post(2, "Vasa", "Middle Java Job", "2019-10-28"));
        posts.put(3, new Post(3, "Ruslan", "Senior Java Job", "2022-06-01"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}