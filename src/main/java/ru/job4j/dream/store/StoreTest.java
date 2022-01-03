package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class StoreTest {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        store.save(new Post(0, "Petr", "Junior Java Job", "2021-12-14"));
        System.out.println("------------------");
        System.out.println("      post ");
        System.out.println("------------------");
        for (Post post : store.findAllPosts()) {
            System.out.println(post);
            System.out.println("------------------");
        }
        store.save(new Post(18, "Petr---", "Junior Java Job---", "2021-12-14"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post);
            System.out.println("------------------");
        }

        store.saveCandidate(new Candidate(0, "Junior Java"));
        System.out.println("   candidate ");
        System.out.println("------------------");
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate);
            System.out.println("------------------");
        }
        store.saveCandidate(new Candidate(18, "Junior Java------------"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate);
            System.out.println("------------------");
        }
    }
}