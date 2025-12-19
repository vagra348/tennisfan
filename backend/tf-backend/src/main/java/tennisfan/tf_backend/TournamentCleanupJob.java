package tennisfan.tf_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tennisfan.tf_backend.services.TournamentValidationService;

import java.util.List;

@Component
public class TournamentCleanupJob {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TournamentValidationService tournamentValidationService;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupTournaments() {
        String sql = """
            SELECT id FROM tournaments
            WHERE status IN ('UPCOMING', 'REGISTRATION_OPEN')
              AND registration_deadline < CURRENT_TIMESTAMP
            """;

        List<Integer> tournamentIds = jdbcTemplate.queryForList(sql, Integer.class);

        for (Integer tournamentId : tournamentIds) {
            tournamentValidationService.cancelTournamentIfNeeded(tournamentId);
        }
    }
}