package tennisfan.tf_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {
    private Integer tournamentId;
    private Integer player1Id;
    private Integer player2Id;
    private Integer player1Score;
    private Integer player2Score;
    private LocalDateTime matchDate;
    private String status;
}