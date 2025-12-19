package tennisfan.tf_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {
    @NotBlank
    @Pattern(regexp = "USER|MEMBER|ADMIN", message = "Role must be USER, MEMBER or ADMIN")
    private String role;
}