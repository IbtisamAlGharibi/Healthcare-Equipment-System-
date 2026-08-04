package com.example.HealthcareEquipmentSystem.Controllers;
import com.example.HealthcareEquipmentSystem.DTO.Requests.LoginRequest;
import com.example.HealthcareEquipmentSystem.DTO.Requests.RegisterRequest;
import com.example.HealthcareEquipmentSystem.DTO.Responses.UserResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Role;
import com.example.HealthcareEquipmentSystem.Entities.User;
import com.example.HealthcareEquipmentSystem.Repositories.UserRepository;
import com.example.HealthcareEquipmentSystem.Utility.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Locked to ROLE_ADMIN in SecurityConfig — this is how the admin creates
    // technician/staff/admin login accounts. Nobody can self-register.
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Role.valueOf(request.getRole().toUpperCase())
        );

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // Admin-only: list every login account so the admin can see who already
    // has access before creating a new one.
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(users);
    }

    // Admin-only: revoke a login account.
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        System.out.println("User exists: " + (user != null));

        if (user != null) {
            System.out.println("Password matches: " +
                    passwordEncoder.matches(request.getPassword(), user.getPassword()));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();   // <-- print the real exception
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
