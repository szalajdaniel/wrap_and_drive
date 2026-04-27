package wrap_and_drive.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wrap_and_drive.Repository.UserRepository;
import wrap_and_drive.Model.User;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPasswordHash().equals(password)) {
            User loggedInUser = userOptional.get();
            return ResponseEntity.ok(Map.of("status", "success", "role", loggedInUser.getRole(), "username", loggedInUser.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "error"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error"));
        }
        userRepository.save(new User(username, email, "USER", password));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success"));
    }
}