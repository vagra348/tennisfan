package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.dto.MatchDTO;

import java.util.List;

@Repository
public class MatchRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FavoritePlayerRepository favoriteRepository;

    private final RowMapper<MatchDTO> matchDTORowMapper = (rs, rowNum) -> {
        MatchDTO dto = new MatchDTO();
        dto.setId(rs.getInt("id"));
        dto.setTournamentName(rs.getString("tournament_name"));
        dto.setPlayer1Id(rs.getInt("player1_id"));
        dto.setPlayer1Name(rs.getString("player1_name"));
        dto.setPlayer2Id(rs.getInt("player2_id"));
        dto.setPlayer2Name(rs.getString("player2_name"));
        dto.setPlayer1Score(rs.getInt("player1_score"));
        if (rs.wasNull()) dto.setPlayer1Score(null);
        dto.setPlayer2Score(rs.getInt("player2_score"));
        if (rs.wasNull()) dto.setPlayer2Score(null);

        java.sql.Timestamp timestamp = rs.getTimestamp("match_date");
        if (timestamp != null) {
            dto.setMatchDate(timestamp.toLocalDateTime());
        }

        dto.setStatus(rs.getString("status"));
        return dto;
    };

    public List<MatchDTO> findAllMatches(Integer userId) {
        String sql = """
            SELECT m.id, t.name as tournament_name, 
                   p1.id as player1_id, p1.full_name as player1_name, 
                   p2.id as player2_id, p2.full_name as player2_name,
                   m.player1_score, m.player2_score, m.match_date, m.status
            FROM matches m
            JOIN tournaments t ON m.tournament_id = t.id
            JOIN players p1 ON m.player1_id = p1.id
            JOIN players p2 ON m.player2_id = p2.id
            ORDER BY m.match_date DESC
            """;

        List<MatchDTO> matches = jdbcTemplate.query(sql, matchDTORowMapper);

        if (userId != null) {
            matches.forEach(match -> {
                match.setPlayer1Favorite(favoriteRepository.isFavorite(userId, match.getPlayer1Id()));
                match.setPlayer2Favorite(favoriteRepository.isFavorite(userId, match.getPlayer2Id()));
            });
        }

        return matches;
    }

    public List<MatchDTO> findMatchesByFavoritePlayers(Integer userId) {
        String sql = """
            SELECT DISTINCT m.id, t.name as tournament_name, 
                   p1.id as player1_id, p1.full_name as player1_name, 
                   p2.id as player2_id, p2.full_name as player2_name,
                   m.player1_score, m.player2_score, m.match_date, m.status
            FROM matches m
            JOIN tournaments t ON m.tournament_id = t.id
            JOIN players p1 ON m.player1_id = p1.id
            JOIN players p2 ON m.player2_id = p2.id
            WHERE p1.id IN (SELECT player_id FROM favorite_players WHERE user_id = ?)
               OR p2.id IN (SELECT player_id FROM favorite_players WHERE user_id = ?)
            ORDER BY m.match_date DESC
            """;

        List<MatchDTO> matches = jdbcTemplate.query(sql, matchDTORowMapper, userId, userId);

        matches.forEach(match -> {
            match.setPlayer1Favorite(favoriteRepository.isFavorite(userId, match.getPlayer1Id()));
            match.setPlayer2Favorite(favoriteRepository.isFavorite(userId, match.getPlayer2Id()));
        });

        return matches;
    }
}