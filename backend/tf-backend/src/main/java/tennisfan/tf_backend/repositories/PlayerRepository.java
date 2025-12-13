package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.entities.Player;

import java.util.List;

@Repository
public class PlayerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Player> playerRowMapper = (rs, rowNum) -> {
        Player player = new Player();
        player.setId(rs.getInt("id"));
        player.setFullName(rs.getString("full_name"));
        player.setCountry(rs.getString("country"));
        player.setRanking(rs.getInt("ranking"));
        if (rs.wasNull()) player.setRanking(null);
        return player;
    };

    public List<Player> findAll() {
        String sql = "SELECT * FROM players ORDER BY ranking NULLS LAST";
        return jdbcTemplate.query(sql, playerRowMapper);
    }
}