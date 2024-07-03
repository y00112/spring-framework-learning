package com.zhaoyss.service;

import com.zhaoyss.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // T execute(ConnectionCallback<T> action)
    public User getUserById(long id) {
        // 注意传入的是ConnectionCallback:
        return jdbcTemplate.execute((Connection conn) -> {
            // 可以直接使用conn实例，不要释放它，回调结束后JdbcTemplate自动释放:
            // 在内部手动创建的PreparedStatement、ResultSet必须用try(...)释放:
            try (var ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
                ps.setObject(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new User( // new User object:
                                rs.getLong("id"), // id
                                rs.getString("email"), // email
                                rs.getString("password"), // password
                                rs.getString("name")); // name
                    }
                    throw new RuntimeException("user not found by id.");
                }
            }
        });
    }

    // T execute(String sql, PreparedStatementCallback<T> action)
    public User getUserByName(String name) {
        // 传入SQL语句，以及PreparedStatementCallback
        return jdbcTemplate.execute("SELECT * FROM users WHERE name =?", (PreparedStatement ps) -> {
            // PreparedStatement 实例已经由JdbcTemplate创建，并在回调后自动释放
            ps.setObject(1, name);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name")
                    );
                }
                throw new RuntimeException("user not found by id.");
            }
        });
    }

    // T queryForObject(String sql, RowMapper<T> rowMapper, Object... args)
    public User getUserByEmail(String email) {
        // 传入SQL，参数和RowMapper实例:
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?",
                (ResultSet rs, int rowNum) -> {
                    // 将ResultSet的当前行映射为一个JavaBean:
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                },
                email);
    }

    /**
     * RomMapper 不一定返回JavaBean，实际上可以返回任何Java对象，使用SELECT COUNT(*)查询时，可以返回Long；
     */
    public long getUsers() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", (ResultSet rs, int rowNum) -> {
            return rs.getLong(1);
        });
    }

    public List<User> getUsers(int pageIndex) {
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?",
                new BeanPropertyRowMapper<>(User.class),
                limit, offset);
    }

    public void updateUser(User user) {
        // 传入SQL，SQL参数，返回更新的行数:
        if (1 != jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by id");
        }
    }

    public User register(String email, String password, String name) {
        // 创建一个KeyHolder:
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update(
                // 参数1:PreparedStatementCreator
                (conn) -> {
                    // 创建PreparedStatement时，必须指定RETURN_GENERATED_KEYS:
                    var ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, email);
                    ps.setObject(2, password);
                    ps.setObject(3, name);
                    return ps;
                },
                // 参数2:KeyHolder
                holder)
        ) {
            throw new RuntimeException("Insert failed.");
        }
        // 从KeyHolder中获取返回的自增值:
        return new User(holder.getKey().longValue(), email, password, name);
    }

}
