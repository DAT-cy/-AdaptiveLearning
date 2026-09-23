package adaptivelearning;

import lombok.RequiredArgsConstructor;
import adaptivelearning.module.users.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AdaptiveLearningApplication implements CommandLineRunner {

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(adaptivelearning.AdaptiveLearningApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userService.initAdminUser();
    }
} 