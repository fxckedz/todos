package br.com.medeiros.api.todo.v1.controllers.auth;

import br.com.medeiros.api.todo.v1.data.LoginResponse;
import br.com.medeiros.api.todo.v1.data.RegisterDto;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.enums.Role;
import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.jwt.JwtUtil;
import br.com.medeiros.api.todo.v1.repositories.UserRepository;
import br.com.medeiros.api.todo.v1.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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

    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
                 produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})

    @Operation(
            summary = "Register a new user",
            description = "Register a new User by passing JSON, XML or YAML",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "User successfully created", responseCode = "201", content =  @Content(schema = @Schema(type = "string", example = "token=eyJhbGciOiJIUzI1..."))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            })
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto dto) {
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
