package adaptivelearning.utils;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.users.entity.Role;
import adaptivelearning.module.users.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static boolean isSecure() {
        return getCurrentAuthentication() != null && getCurrentAuthentication().isAuthenticated();
    }

    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getUserId();
        } else {
            throw new CommonException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }

    public static String getCurrentUsername(){
        User user = getCurrentUser();
        if (user != null) {
            return user.getUsername();
        } else {
            throw new CommonException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }

    public static User getCurrentUser() {
        if(getCurrentAuthentication() != null)
            return (User) getCurrentAuthentication().getPrincipal();
        return null;
    }

    private static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean hasRole(Role role) {
        return getCurrentAuthentication().getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role.name()));
    }

    public static boolean hasRole(Role... role){
        for(Role r : role){
            if(hasRole(r)){
                return true;
            }
        }
        return false;
    }
}
