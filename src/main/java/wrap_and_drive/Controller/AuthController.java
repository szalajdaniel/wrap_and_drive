package wrap_and_drive.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wrap_and_drive.Model.User;
import wrap_and_drive.Repository.UserRepository;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) { return null; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        String hashedInput = hashPassword(password);
        return userRepository.findByEmail(email)
                .filter(u -> u.getPasswordHash().equals(hashedInput))
                .map(u -> ResponseEntity.ok(Map.of(
                        "status", "success",
                        "username", u.getUsername(),
                        "role", u.getRole()
                )))
                .orElse(ResponseEntity.status(401).body(Map.of("status", "error")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try {
            // Sprawdzamy czy mail już istnieje
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email jest zajęty"));
            }

            // Tworzymy nowego użytkownika - rola domyślna USER
            User newUser = new User(username, email, "USER", hashPassword(password));
            userRepository.save(newUser);

            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            // To wypisze błąd w Twojej konsoli IntelliJ
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}