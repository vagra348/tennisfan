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
        String sql = "SELECT id, name, location FROM tournaments ORDER BY name";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Tournament(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                )
        );
    }
}