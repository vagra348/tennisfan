package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FavoritePlayerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addFavorite(Integer userId, Integer playerId) {
        String sql = "INSERT INTO favorite_players (user_id, player_id) VALUES (?, ?) " +
                "ON CONFLICT (user_id, player_id) DO NOTHING";
        jdbcTemplate.update(sql, userId, playerId);
    }

    public void removeFavorite(Integer userId, Integer playerId) {
        String sql = "DELETE FROM favorite_players WHERE user_id = ? AND player_id = ?";
        jdbcTemplate.update(sql, userId, playerId);
    }

    public boolean isFavorite(Integer userId, Integer playerId) {
        String sql = "SELECT COUNT(*) FROM favorite_players WHERE user_id = ? AND player_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, playerId);
        return count != null && count > 0;
    }
}