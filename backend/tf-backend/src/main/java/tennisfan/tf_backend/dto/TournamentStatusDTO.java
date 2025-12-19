package tennisfan.tf_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentStatusDTO {
    private Integer tournamentId;
    private String tournamentName;
    private boolean isRegistered;
    private boolean registrationOpen;
    private LocalDateTime registrationDeadline;
    private String message;
    private String status;
    private boolean canRegister;
}