package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class TournamentValidationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void validateTournamentRegistration(Integer userId, Integer tournamentId) {
        String checkRoleSql = "SELECT role FROM users WHERE id = ?";
        String role = jdbcTemplate.queryForObject(checkRoleSql, String.class, userId);

        if (!"MEMBER".equals(role)) {
            throw new SecurityException("Только участники клуба могут регистрироваться на турниры");
        }

        String checkTournamentSql = """
            SELECT status, registration_deadline
            FROM tournaments WHERE id = ?
            """;

        var tournamentData = jdbcTemplate.queryForMap(checkTournamentSql, tournamentId);
        String status = (String) tournamentData.get("status");
        LocalDateTime deadline = tournamentData.get("registration_deadline") != null ?
                ((java.sql.Timestamp) tournamentData.get("registration_deadline")).toLocalDateTime() : null;

        if (!"REGISTRATION_OPEN".equals(status)) {
            throw new IllegalArgumentException("Регистрация на этот турнир закрыта");
        }

        if (deadline != null && LocalDateTime.now().isAfter(deadline)) {
            throw new IllegalArgumentException("Срок регистрации истек");
        }

        String checkRegistrationSql = """
            SELECT COUNT(*) FROM tournament_participants
            WHERE tournament_id = ? AND user_id = ?
            """;
        Integer count = jdbcTemplate.queryForObject(checkRegistrationSql, Integer.class, tournamentId, userId);

        if (count > 0) {
            throw new IllegalArgumentException("Вы уже зарегистрированы на этот турнир");
        }
    }

    public boolean shouldCancelTournament(Integer tournamentId) {
        String countSql = "SELECT COUNT(*) FROM tournament_participants WHERE tournament_id = ?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, tournamentId);

        String deadlineSql = "SELECT registration_deadline FROM tournaments WHERE id = ?";

        try {
            LocalDateTime deadline = jdbcTemplate.queryForObject(deadlineSql,
                    (rs, rowNum) -> rs.getTimestamp("registration_deadline") != null ?
                            rs.getTimestamp("registration_deadline").toLocalDateTime() : null,
                    tournamentId);

            return deadline != null &&
                    LocalDateTime.now().isAfter(deadline) &&
                    count < 2;
        } catch (Exception e) {
            return false;
        }
    }

    public String determineTournamentStatusAfterRegistrationClose(Integer tournamentId) {
        String countSql = "SELECT COUNT(*) FROM tournament_participants WHERE tournament_id = ?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, tournamentId);

        if (count < 2) {
            return "CANCELLED";
        } else {
            return "ONGOING";
        }
    }

    @Transactional
    public void cancelTournamentIfNeeded(Integer tournamentId) {
        if (shouldCancelTournament(tournamentId)) {
            String cancelSql = "UPDATE tournaments SET status = 'CANCELLED' WHERE id = ?";
            jdbcTemplate.update(cancelSql, tournamentId);
        }
    }
}