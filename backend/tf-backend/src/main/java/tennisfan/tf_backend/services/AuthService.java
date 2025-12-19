package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.dto.AuthRequest;
import tennisfan.tf_backend.dto.AuthResponse;
import tennisfan.tf_backend.entities.User;
import tennisfan.tf_backend.repositories.UserRepository;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse login(AuthRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return new AuthResponse(null, null, null, "User not found");
        }

        User user = userOpt.get();

        String expectedPassword = request.getUsername() + "Pass";
        if (!expectedPassword.equals(request.getPassword())) {
            return new AuthResponse(null, null, null, "Invalid password");
        }

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                "Login successful"
        );
    }

    public AuthResponse register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse(null, null, null, "Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPasswordHash(request.getUsername() + "Pass");
        newUser.setRole("USER");
        newUser.setFullName(request.getUsername());

        User savedUser = userRepository.save(newUser);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole(),
                "Registration successful"
        );
    }
}