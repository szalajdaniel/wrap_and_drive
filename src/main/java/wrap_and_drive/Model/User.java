package wrap_and_drive.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(name = "password_hash", nullable = false) // Musi być dokładnie tak jak w XML
    private String passwordHash;

    // 1. PUSTY KONSTRUKTOR (BEZWZGLĘDNIE WYMAGANY)
    public User() {}

    // 2. KONSTRUKTOR Z PARAMETRAMI
    public User(String username, String email, String role, String passwordHash) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    // GETTERY I SETTERY
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}