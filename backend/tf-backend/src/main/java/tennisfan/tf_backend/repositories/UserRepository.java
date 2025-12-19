package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.entities.User;

import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        return user;
    };

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public User save(User user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO users (username, password_hash, role, full_name) VALUES (?, ?, ?, ?) RETURNING id";
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                    user.getUsername(),
                    user.getPasswordHash(),
                    user.getRole() != null ? user.getRole() : "USER",
                    user.getFullName() != null ? user.getFullName() : user.getUsername());
            user.setId(id);
        } else {
            String sql = "UPDATE users SET username = ?, password_hash = ?, role = ?, full_name = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    user.getUsername(),
                    user.getPasswordHash(),
                    user.getRole(),
                    user.getFullName(),
                    user.getId());
        }
        return user;
    }
}