package tennisfan.tf_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Integer id;
    private String tournamentName;
    private Integer player1Id;
    private String player1Name;
    private Integer player2Id;
    private String player2Name;
    private Integer player1Score;
    private Integer player2Score;
    private LocalDateTime matchDate;
    private String status;
    private boolean player1Favorite;
    private boolean player2Favorite;
}