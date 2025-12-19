package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.dto.PlayerDTO;

import java.util.List;

@Repository
public class PlayerViewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PlayerDTO> findAllClubPlayers() {
        String sql = """
            SELECT u.id, u.full_name, u.username
            FROM users u
            WHERE u.role = 'MEMBER'
            ORDER BY u.full_name
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PlayerDTO(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username")
                )
        );
    }

    public List<PlayerDTO> findAllPlayers() {
        String sql = """
            SELECT u.id, u.full_name, u.username
            FROM users u
            WHERE u.role IN ('MEMBER', 'ADMIN')
            ORDER BY u.full_name
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PlayerDTO(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username")
                )
        );
    }

    public PlayerDTO findById(Integer id) {
        String sql = """
            SELECT u.id, u.full_name, u.username
            FROM users u
            WHERE u.id = ?
            """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new PlayerDTO(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username")
                ), id);
    }
}
