package adaptivelearning.module.users.dto.response;

import adaptivelearning.module.users.entity.Role;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUser {
    private String username;
    private Role role;
}
