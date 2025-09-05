package br.com.medeiros.api.todo.v1.controllers.auth;

import br.com.medeiros.api.todo.v1.data.LoginResponse;
import br.com.medeiros.api.todo.v1.data.RegisterDto;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.enums.Role;
import br.com.medeiros.api.todo.v1.jwt.JwtUtil;
import br.com.medeiros.api.todo.v1.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos/v1/auth/register")
public class Register {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    public Register(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        if(userRepository.findByUsername(dto.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

}
