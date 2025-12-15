package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.dto.AdminMatchDTO;
import tennisfan.tf_backend.dto.AdminPlayerDTO;
import tennisfan.tf_backend.dto.AdminUserDTO;
import tennisfan.tf_backend.dto.CreateMatchRequest;
import tennisfan.tf_backend.dto.CreatePlayerRequest;

import java.util.List;

@Repository
public class AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AdminPlayerDTO> getAllPlayersForAdmin() {
        String sql = "SELECT id, full_name, country, ranking FROM players ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AdminPlayerDTO(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("country"),
                        rs.getInt("ranking")
                )
        );
    }

    public AdminPlayerDTO createPlayer(CreatePlayerRequest request) {
        String sql = "INSERT INTO players (full_name, country, ranking) VALUES (?, ?, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                request.getFullName(),
                request.getCountry(),
                request.getRanking());

        return new AdminPlayerDTO(id, request.getFullName(), request.getCountry(), request.getRanking());
    }

    public void updatePlayer(Integer id, CreatePlayerRequest request) {
        String sql = "UPDATE players SET full_name = ?, country = ?, ranking = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                request.getFullName(),
                request.getCountry(),
                request.getRanking(),
                id);
    }

    public void deletePlayer(Integer id) {
        String deleteFavoritesSql = "DELETE FROM favorite_players WHERE player_id = ?";
        jdbcTemplate.update(deleteFavoritesSql, id);

        String deleteMatchesSql = "DELETE FROM matches WHERE player1_id = ? OR player2_id = ?";
        jdbcTemplate.update(deleteMatchesSql, id, id);

        String deletePlayerSql = "DELETE FROM players WHERE id = ?";
        jdbcTemplate.update(deletePlayerSql, id);
    }

    public List<AdminMatchDTO> getAllMatchesForAdmin() {
        String sql = """
            SELECT id, tournament_id, player1_id, player2_id,
                   player1_score, player2_score, match_date, status
            FROM matches ORDER BY match_date DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AdminMatchDTO(
                        rs.getInt("id"),
                        rs.getInt("tournament_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getObject("player1_score") != null ? rs.getInt("player1_score") : null,
                        rs.getObject("player2_score") != null ? rs.getInt("player2_score") : null,
                        rs.getTimestamp("match_date").toLocalDateTime(),
                        rs.getString("status")
                )
        );
    }

    public AdminMatchDTO createMatch(CreateMatchRequest request) {
        if (request.getPlayer1Id().equals(request.getPlayer2Id())) {
            throw new IllegalArgumentException("Players must be different");
        }

        String sql = """
            INSERT INTO matches (tournament_id, player1_id, player2_id,
                                player1_score, player2_score, match_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id
            """;

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                request.getTournamentId(),
                request.getPlayer1Id(),
                request.getPlayer2Id(),
                request.getPlayer1Score(),
                request.getPlayer2Score(),
                request.getMatchDate(),
                request.getStatus());

        return new AdminMatchDTO(id,
                request.getTournamentId(),
                request.getPlayer1Id(),
                request.getPlayer2Id(),
                request.getPlayer1Score(),
                request.getPlayer2Score(),
                request.getMatchDate(),
                request.getStatus());
    }

    public void updateMatch(Integer id, CreateMatchRequest request) {
        if (request.getPlayer1Id().equals(request.getPlayer2Id())) {
            throw new IllegalArgumentException("Players must be different");
        }

        String sql = """
            UPDATE matches SET tournament_id = ?, player1_id = ?, player2_id = ?, 
                            player1_score = ?, player2_score = ?, match_date = ?, status = ? 
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                request.getTournamentId(),
                request.getPlayer1Id(),
                request.getPlayer2Id(),
                request.getPlayer1Score(),
                request.getPlayer2Score(),
                request.getMatchDate(),
                request.getStatus(),
                id);
    }

    public void deleteMatch(Integer id) {
        String sql = "DELETE FROM matches WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<AdminUserDTO> getAllUsers() {
        String sql = "SELECT id, username, role FROM users ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AdminUserDTO(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                )
        );
    }

    public void updateUserRole(Integer userId, String role) {
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Invalid role. Must be USER or ADMIN");
        }

        String sql = "UPDATE users SET role = ? WHERE id = ?";
        jdbcTemplate.update(sql, role, userId);
    }

    public void deleteUser(Integer userId) {
        String deleteFavoritesSql = "DELETE FROM favorite_players WHERE user_id = ?";
        jdbcTemplate.update(deleteFavoritesSql, userId);

        String deleteUserSql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(deleteUserSql, userId);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}