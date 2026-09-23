package adaptivelearning.module.users.service;

import adaptivelearning.module.users.dto.request.LoginRequest;
import adaptivelearning.module.users.dto.request.SignUpRequest;
import adaptivelearning.module.users.dto.response.LoginResponse;
import adaptivelearning.module.users.dto.response.ProfileUser;

public interface UserService {
    LoginResponse login(LoginRequest request);
    void initAdminUser();
    Boolean signUp(SignUpRequest request);
    ProfileUser getProfile();
}