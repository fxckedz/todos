package br.com.medeiros.api.todo.v1.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(

        @NotBlank
        @Size(min = 3, message= "username must have at least 3 characters")
        String username,

        @NotBlank
        @Size(min = 8, message= "password must have at least 8 characters")
        String password,

        @NotBlank
        String passwordConfirmation
) {
}
