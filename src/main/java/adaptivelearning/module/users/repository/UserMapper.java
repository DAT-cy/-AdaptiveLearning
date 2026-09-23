package adaptivelearning.module.users.repository;

import adaptivelearning.module.users.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByUsername(String username);
    int countAdminUsers();
    void insertUser(User user);
} 