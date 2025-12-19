package tennisfan.tf_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDTO {
    private Integer id;
    private String name;
    private LocalDateTime registrationDeadline;
    private String status;
    private Integer registeredCount;
    private boolean canStart;
}