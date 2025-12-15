package tennisfan.tf_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerRequest {
    private String fullName;
    private String country;
    private Integer ranking;
}