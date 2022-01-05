package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.User;

public class DbStore implements Store {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbStore.class.getSimpleName());
    private final BasicDataSource pool = new BasicDataSource();

    private DbStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new InputStreamReader(DbStore.class.getClassLoader()
                .getResourceAsStream("db.properties")))) {
            cfg.load(io);
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            throw new IllegalStateException(e);
        }

        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            throw new IllegalStateException(e);
        }

        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new DbStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    public int regUser(String name, String email, String password) {
        var rsl = findUserForEmail(email);
        if (rsl.getName().isEmpty()) {
            return -1;
        } else {
            var req = "INSERT INTO userWEB(name, email, password) VALUES (?, ?, ?)";
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps = cn.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.execute();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        return id.getInt(1);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
                e.printStackTrace();
            }
            return -1;
        }
    }

    public User findUserForEmail(String email) {
        var req = "SELECT * FROM userWEB WHERE email = ?";
        User usr = new User();
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    usr.setId(it.getInt("id"));
                    usr.setName(it.getString("name"));
                    usr.setEmail(it.getString("email"));
                    usr.setPassword(it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return usr;
    }

    public User findUserForId(int id) {
        var req = "SELECT * FROM userWEB WHERE id = ?";
        User usr = new User();
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    usr.setId(it.getInt("id"));
                    usr.setName(it.getString("name"));
                    usr.setEmail(it.getString("email"));
                    usr.setPassword(it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return usr;
    }

    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getString("created")));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return posts;
    }

    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return candidates;
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name, description, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return post;
    }

    private Candidate createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return candidate;
    }

    private void update(Post post) {
        var req = "UPDATE post SET name = ?, description = ?, created = ? WHERE id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getCreated());
            ps.setInt(4, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCandidate(Candidate candidate) {
        var req = "UPDATE candidate SET name = ? WHERE id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        var req = "DELETE from post where id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
    }

    public void deleteCandidate(int id) {
        var req = "DELETE from candidate where id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
    }

    public Post findByName(String name) {
        var req = "SELECT * FROM post WHERE name = ?";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(req)
        ) {
            ps.setString(1, name);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getString("created"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getString("created"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    public Candidate findByIdCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }
}