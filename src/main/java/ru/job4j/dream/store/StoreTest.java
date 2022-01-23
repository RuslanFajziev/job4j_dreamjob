package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.time.LocalDateTime;

public class StoreTest {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        store.save(new Post(0, "Petr", "Junior Java Job", new java.util.Date()));
        System.out.println("------------------");
        System.out.println("      post ");
        System.out.println("------------------");
        for (Post post : store.findAllPosts(false)) {
            System.out.println(post);
            System.out.println("------------------");
        }
        store.save(new Post(18, "Petr---", "Junior Java Job---", new java.util.Date()));
        for (Post post : store.findAllPosts(false)) {
            System.out.println(post);
            System.out.println("------------------");
        }

        store.saveCandidate(new Candidate(0, "Junior Java", "Omsk", new java.util.Date()));
        System.out.println("   candidate ");
        System.out.println("------------------");
        for (Candidate candidate : store.findAllCandidates(false)) {
            System.out.println(candidate);
            System.out.println("------------------");
        }
        store.saveCandidate(new Candidate(18, "Junior Java------------", "Uhta", new java.util.Date()));
        for (Candidate candidate : store.findAllCandidates(false)) {
            System.out.println(candidate);
            System.out.println("------------------");
        }
    }
}