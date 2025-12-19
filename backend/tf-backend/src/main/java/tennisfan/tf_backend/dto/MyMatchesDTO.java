package tennisfan.tf_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMatchesDTO {
    private Integer matchId;
    private Integer tournamentId;
    private String tournamentName;
    private String opponentName;
    private String opponentUsername;
    private LocalDateTime scheduledTime;
    private String status;
    private Integer player1Score;
    private Integer player2Score;
}