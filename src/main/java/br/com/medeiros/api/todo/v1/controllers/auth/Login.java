package br.com.medeiros.api.todo.v1.controllers.auth;

import br.com.medeiros.api.todo.v1.data.LoginDto;
import br.com.medeiros.api.todo.v1.data.LoginResponse;
import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.jwt.JwtUtil;
import br.com.medeiros.api.todo.v1.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos/v1/auth/login")
public class Login {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    public Login(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        UserEntity user = userRepository.findByUsername(dto.username()).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found"));


        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
