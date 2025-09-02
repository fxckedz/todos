package br.com.medeiros.api.todo.v1.data;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto(

        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
