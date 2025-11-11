package br.com.medeiros.api.todo.v1.controllers.auth;

import br.com.medeiros.api.todo.v1.data.LoginDto;
import br.com.medeiros.api.todo.v1.data.LoginResponse;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.jwt.JwtUtil;
import br.com.medeiros.api.todo.v1.repositories.UserRepository;
import br.com.medeiros.api.todo.v1.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Login", description = "Login to a User")
public class Login {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    public Login(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
                 produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})

    @Operation(
            summary = "Login to a user",
            description = "Login to User by passing JSON, XML or YAML",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "User Logged", responseCode = "201", content =  @Content(schema = @Schema(type = "string", example = "token=eyJhbGciOiJIUzI1..."))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            })
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto dto) {
        UserEntity user = userRepository.findByUsername(dto.username()).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found"));


        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
