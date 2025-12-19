package tennisfan.tf_backend.repositories;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.dto.*;
import tennisfan.tf_backend.services.TournamentValidationService;

import java.util.List;

@Repository
public class AdminRepository {

    @Getter
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TournamentValidationService tournamentValidationService;

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
        if (!role.equals("USER") && !role.equals("MEMBER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Invalid role. Must be USER, MEMBER or ADMIN");
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

    public List<TournamentDTO> getAllTournaments() {
        String sql = """
        SELECT t.id, t.name, t.registration_deadline, t.status,
               (SELECT COUNT(*) FROM tournament_participants tp WHERE tp.tournament_id = t.id) as registered_count
        FROM tournaments t
        ORDER BY t.registration_deadline DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TournamentDTO dto = new TournamentDTO();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));

            if (rs.getTimestamp("registration_deadline") != null) {
                dto.setRegistrationDeadline(rs.getTimestamp("registration_deadline").toLocalDateTime());
            }

            dto.setStatus(rs.getString("status"));
            dto.setRegisteredCount(rs.getInt("registered_count"));

            dto.setCanStart(rs.getInt("registered_count") >= 2);

            return dto;
        });
    }

    public TournamentDTO createTournament(CreateTournamentDTO request) {
        String sql = """
        INSERT INTO tournaments (name, registration_deadline, status)
        VALUES (?, ?, 'UPCOMING')
        RETURNING id
        """;

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                request.getName(),
                request.getRegistrationDeadline());

        return getTournamentById(id);
    }

    private TournamentDTO getTournamentById(Integer id) {
        String sql = """
        SELECT t.id, t.name, t.registration_deadline, t.status,
               (SELECT COUNT(*) FROM tournament_participants tp WHERE tp.tournament_id = t.id) as registered_count
        FROM tournaments t
        WHERE t.id = ?
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            TournamentDTO dto = new TournamentDTO();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));

            if (rs.getTimestamp("registration_deadline") != null) {
                dto.setRegistrationDeadline(rs.getTimestamp("registration_deadline").toLocalDateTime());
            }

            dto.setStatus(rs.getString("status"));
            dto.setRegisteredCount(rs.getInt("registered_count"));
            dto.setCanStart(rs.getInt("registered_count") >= 2);

            return dto;
        }, id);
    }

    public void updateTournament(Integer tournamentId, UpdateTournamentDTO request) {
        String sql = """
        UPDATE tournaments
        SET name = ?, registration_deadline = ?, status = ?
        WHERE id = ?
        """;

        jdbcTemplate.update(sql,
                request.getName(),
                request.getRegistrationDeadline(),
                request.getStatus(),
                tournamentId);
    }

    public void deleteTournament(Integer tournamentId) {
        String deleteParticipantsSql = "DELETE FROM tournament_participants WHERE tournament_id = ?";
        String deleteMatchesSql = "DELETE FROM matches WHERE tournament_id = ?";
        String deleteTournamentSql = "DELETE FROM tournaments WHERE id = ?";

        jdbcTemplate.update(deleteParticipantsSql, tournamentId);
        jdbcTemplate.update(deleteMatchesSql, tournamentId);
        jdbcTemplate.update(deleteTournamentSql, tournamentId);
    }

    public void openTournamentRegistration(Integer tournamentId) {
        String sql = "UPDATE tournaments SET status = 'REGISTRATION_OPEN' WHERE id = ?";
        jdbcTemplate.update(sql, tournamentId);
    }

    public void closeTournamentRegistration(Integer tournamentId) {
        String newStatus = tournamentValidationService.determineTournamentStatusAfterRegistrationClose(tournamentId);

        String sql = "UPDATE tournaments SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, newStatus, tournamentId);
    }

    public List<TournamentParticipantDTO> getTournamentParticipants(Integer tournamentId) {
        String sql = """
        SELECT u.id as user_id, u.username, u.full_name,
               tp.registration_date
        FROM tournament_participants tp
        JOIN users u ON tp.user_id = u.id
        WHERE tp.tournament_id = ?
        ORDER BY tp.registration_date
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TournamentParticipantDTO dto = new TournamentParticipantDTO();
            dto.setUserId(rs.getInt("user_id"));
            dto.setUsername(rs.getString("username"));
            dto.setFullName(rs.getString("full_name"));

            if (rs.getTimestamp("registration_date") != null) {
                dto.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
            }

            return dto;
        }, tournamentId);
    }

}