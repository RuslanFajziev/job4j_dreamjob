package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.User;

public class DbStore implements Store {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbStore.class.getSimpleName());
    private final BasicDataSource pool = new BasicDataSource();

    private DbStore() {
        var fileProperties = "db.properties";
        var fileProperties2 = "dbH2.properties";
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new InputStreamReader(
                DbStore.class.getClassLoader().getResourceAsStream(fileProperties)))) {
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
        if (rsl != null) {
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
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    User usr = new User();
                    usr.setId(it.getInt("id"));
                    usr.setName(it.getString("name"));
                    usr.setEmail(it.getString("email"));
                    usr.setPassword(it.getString("password"));
                    return usr;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
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

    public List<Post> findAllPosts(Boolean today) {
        var queryAll = "SELECT * FROM Post ORDER BY id";
        var queryTodayAll = "SELECT * FROM Post WHERE create_date BETWEEN CURRENT_DATE AND CURRENT_DATE + 1 ORDER BY id";
        var query = today ? queryTodayAll : queryAll;
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), (it.getDate("create_date"))));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return posts;
    }

    public List<Candidate> findAllCandidates(Boolean today) {
        var queryAll = "SELECT * FROM Candidate ORDER BY id";
        var queryTodayAll = "SELECT * FROM candidate WHERE create_date BETWEEN CURRENT_DATE AND CURRENT_DATE + 1 ORDER BY id";
        var query = today ? queryTodayAll : queryAll;
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"),
                            it.getString("cityName"), it.getDate("create_date")));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return candidates;
    }

    public List<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city ORDER BY id")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return cities;
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

    public void saveCity(City city) {
        if (city.getId() == 0) {
            createCity(city);
        } else {
            updateCity(city);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name, description, create_date) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, new Timestamp(post.getCreateDate().getTime()));
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
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name, cityName, create_date) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getCityName());
            ps.setTimestamp(3, new Timestamp(candidate.getCreateDate().getTime()));
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

    private City createCity(City city) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO city(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, city.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    city.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return city;
    }

    private void update(Post post) {
        var req = "UPDATE post SET name = ?, description = ?, create_date = ? WHERE id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, new Timestamp(post.getCreateDate().getTime()));
            ps.setInt(4, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCandidate(Candidate candidate) {
        var req = "UPDATE candidate SET name = ?, cityName = ?, create_date = ?  WHERE id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getCityName());
            ps.setTimestamp(3, new Timestamp(candidate.getCreateDate().getTime()));
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
    }

    private void updateCity(City city) {
        var req = "UPDATE city SET name = ? WHERE id = ?";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, city.getName());
            ps.setInt(2, city.getId());
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

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getDate("create_date"));
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
                    return new Candidate(it.getInt("id"), it.getString("name"),
                            it.getString("cityName"), it.getDate("create_date"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    public City findByIdCity(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new City(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    public Post findByNamePost(String name) {
        var req = "SELECT * FROM post WHERE name = ? limit 1";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, name);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getDate("create_date"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    public Candidate findByNameCandidate(String name) {
        var req = "SELECT * FROM candidate WHERE name = ? limit 1";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.setString(1, name);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(it.getInt("id"), it.getString("name"),
                            it.getString("cityName"), it.getDate("create_date"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    public void wipeTablePost() {
        var req = "DELETE from post";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
    }

    public void wipeTableCandidate() {
        var req = "DELETE from candidate";
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(req)) {
            ps.execute();
        } catch (Exception e) {
            LOGGER.error("Не удалось выполнить операцию: { }", e.getCause());
            e.printStackTrace();
        }
    }
}