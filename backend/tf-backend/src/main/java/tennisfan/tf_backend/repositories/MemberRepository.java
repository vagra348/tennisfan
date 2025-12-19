package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.dto.MyMatchesDTO;
import tennisfan.tf_backend.dto.TournamentStatusDTO;
import tennisfan.tf_backend.services.TournamentValidationService;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TournamentValidationService tournamentValidationService;

    public TournamentStatusDTO getCurrentTournamentStatus(Integer userId) {
        String sql = """
            SELECT t.id, t.name, t.registration_deadline, t.status,
                   EXISTS(SELECT 1 FROM tournament_participants tp
                          WHERE tp.tournament_id = t.id AND tp.user_id = ?) as is_registered
            FROM tournaments t
            WHERE t.status IN ('UPCOMING', 'REGISTRATION_OPEN')
              AND (t.registration_deadline IS NULL OR t.registration_deadline > CURRENT_TIMESTAMP)
            ORDER BY t.registration_deadline
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                TournamentStatusDTO dto = new TournamentStatusDTO();
                dto.setTournamentId(rs.getInt("id"));
                dto.setTournamentName(rs.getString("name"));
                dto.setRegistered(rs.getBoolean("is_registered"));
                dto.setRegistrationOpen("REGISTRATION_OPEN".equals(rs.getString("status")));
                dto.setRegistrationDeadline(rs.getTimestamp("registration_deadline") != null ?
                        rs.getTimestamp("registration_deadline").toLocalDateTime() : null);
                dto.setStatus(rs.getString("status"));

                // Формируем сообщение для пользователя
                String message;
                boolean canRegister = false;

                if (rs.getBoolean("is_registered")) {
                    message = "Вы участвуете в турнире!";
                } else if ("REGISTRATION_OPEN".equals(rs.getString("status"))) {
                    LocalDateTime deadline = rs.getTimestamp("registration_deadline").toLocalDateTime();
                    if (LocalDateTime.now().isBefore(deadline)) {
                        message = String.format("Хочу участвовать в турнире! (Заявки принимаются до %s)",
                                deadline.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                        canRegister = true;
                    } else {
                        message = "Регистрация закрыта";
                    }
                } else {
                    message = "Ожидайте начала набора в следующий турнир";
                }

                dto.setMessage(message);
                dto.setCanRegister(canRegister);
                return dto;
            }, userId);
        } catch (Exception e) {
            // Если нет открытых турниров, возвращаем пустой статус
            TournamentStatusDTO dto = new TournamentStatusDTO();
            dto.setMessage("Ожидайте начала набора в следующий турнир");
            dto.setCanRegister(false);
            dto.setRegistrationOpen(false);
            dto.setRegistered(false);
            return dto;
        }
    }

    public void registerForTournament(Integer userId, Integer tournamentId) {
        tournamentValidationService.validateTournamentRegistration(userId, tournamentId);
        String registerSql = "INSERT INTO tournament_participants (tournament_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(registerSql, tournamentId, userId);
        tournamentValidationService.cancelTournamentIfNeeded(tournamentId);
    }

    public List<MyMatchesDTO> getMyMatches(Integer userId) {
        String sql = """
            SELECT m.id, m.tournament_id, t.name as tournament_name,
                   CASE
                       WHEN m.player1_id = ? THEN u2.full_name
                       WHEN m.player2_id = ? THEN u1.full_name
                   END as opponent_name,
                   CASE
                       WHEN m.player1_id = ? THEN u2.username
                       WHEN m.player2_id = ? THEN u1.username
                   END as opponent_username,
                   m.match_date, m.status, m.player1_score, m.player2_score
            FROM matches m
            JOIN tournaments t ON m.tournament_id = t.id
            JOIN users u1 ON m.player1_id = u1.id
            JOIN users u2 ON m.player2_id = u2.id
            WHERE (m.player1_id = ? OR m.player2_id = ?)
            ORDER BY m.match_date DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MyMatchesDTO dto = new MyMatchesDTO();
            dto.setMatchId(rs.getInt("id"));
            dto.setTournamentId(rs.getInt("tournament_id"));
            dto.setTournamentName(rs.getString("tournament_name"));
            dto.setOpponentName(rs.getString("opponent_name"));
            dto.setOpponentUsername(rs.getString("opponent_username"));

            if (rs.getTimestamp("match_date") != null) {
                dto.setScheduledTime(rs.getTimestamp("match_date").toLocalDateTime());
            }

            dto.setStatus(rs.getString("status"));
            dto.setPlayer1Score(rs.getInt("player1_score"));
            if (rs.wasNull()) dto.setPlayer1Score(null);
            dto.setPlayer2Score(rs.getInt("player2_score"));
            if (rs.wasNull()) dto.setPlayer2Score(null);

            return dto;
        }, userId, userId, userId, userId, userId, userId);
    }

    public List<TournamentStatusDTO> getTournamentHistory(Integer userId) {
        String sql = """
            SELECT t.id, t.name, t.status, t.registration_deadline,
                   EXISTS(SELECT 1 FROM tournament_participants tp
                          WHERE tp.tournament_id = t.id AND tp.user_id = ?) as is_registered
            FROM tournaments t
            WHERE t.status IN ('COMPLETED', 'CANCELLED')
            ORDER BY t.registration_deadline DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TournamentStatusDTO dto = new TournamentStatusDTO();
            dto.setTournamentId(rs.getInt("id"));
            dto.setTournamentName(rs.getString("name"));
            dto.setStatus(rs.getString("status"));
            dto.setRegistered(rs.getBoolean("is_registered"));

            if (rs.getTimestamp("registration_deadline") != null) {
                dto.setRegistrationDeadline(rs.getTimestamp("registration_deadline").toLocalDateTime());
            }

            // Формируем сообщение
            String message;
            if (rs.getBoolean("is_registered")) {
                if ("COMPLETED".equals(rs.getString("status"))) {
                    message = "Вы участвовали в турнире";
                } else {
                    message = "Вы были зарегистрированы, но турнир отменен";
                }
            } else {
                message = "Вы не участвовали в этом турнире";
            }

            dto.setMessage(message);
            dto.setCanRegister(false);
            dto.setRegistrationOpen(false);

            return dto;
        }, userId);
    }
}