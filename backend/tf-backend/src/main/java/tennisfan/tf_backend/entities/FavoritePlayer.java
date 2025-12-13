package tennisfan.tf_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePlayer {
    @EmbeddedId
    private FavoritePlayerId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private Player player;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class FavoritePlayerId implements java.io.Serializable {
    private Integer userId;
    private Integer playerId;
}