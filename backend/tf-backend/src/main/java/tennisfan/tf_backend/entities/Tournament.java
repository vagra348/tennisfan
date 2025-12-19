package tennisfan.tf_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "registration_deadline")
    private LocalDateTime registrationDeadline;

    @Column(nullable = false)
    private String status = "UPCOMING";

    Tournament(int id, String name) {
        this.id = id;
        this.name = name;
    }
}