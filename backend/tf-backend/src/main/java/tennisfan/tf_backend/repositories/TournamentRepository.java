package tennisfan.tf_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tennisfan.tf_backend.entities.Tournament;

import java.util.List;

@Repository
public class TournamentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Tournament> findAll() {
        String sql = "SELECT id, name, registration_deadline, status FROM tournaments ORDER BY registration_deadline DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Tournament(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("registration_deadline") != null ?
                                rs.getTimestamp("registration_deadline").toLocalDateTime() : null,
                        rs.getString("status")
                )
        );
    }

    public List<Tournament> findActiveTournaments() {
        String sql = """
            SELECT id, name, registration_deadline, status
            FROM tournaments
            WHERE status IN ('UPCOMING', 'REGISTRATION_OPEN', 'ONGOING')
            ORDER BY registration_deadline
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Tournament(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("registration_deadline") != null ?
                                rs.getTimestamp("registration_deadline").toLocalDateTime() : null,
                        rs.getString("status")
                )
        );
    }

    public Tournament findById(Integer id) {
        String sql = "SELECT id, name, registration_deadline, status FROM tournaments WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Tournament(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("registration_deadline") != null ?
                                rs.getTimestamp("registration_deadline").toLocalDateTime() : null,
                        rs.getString("status")
                ), id);
    }

    public Tournament save(Tournament tournament) {
        if (tournament.getId() == null) {
            // Insert new tournament
            String sql = "INSERT INTO tournaments (name, registration_deadline, status) VALUES (?, ?, ?) RETURNING id";
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                    tournament.getName(),
                    tournament.getRegistrationDeadline(),
                    tournament.getStatus());
            tournament.setId(id);
        } else {
            // Update existing tournament
            String sql = "UPDATE tournaments SET name = ?, registration_deadline = ?, status = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    tournament.getName(),
                    tournament.getRegistrationDeadline(),
                    tournament.getStatus(),
                    tournament.getId());
        }
        return tournament;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM tournaments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateStatus(Integer tournamentId, String status) {
        String sql = "UPDATE tournaments SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, tournamentId);
    }

    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM tournaments WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public int countParticipants(Integer tournamentId) {
        String sql = "SELECT COUNT(*) FROM tournament_participants WHERE tournament_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, tournamentId);
    }
}