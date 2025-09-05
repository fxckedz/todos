package br.com.medeiros.api.todo.v1.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(

        @NotBlank
        @Size(min = 3)
        String username,

        @NotBlank
        @Size(min = 8)
        String password
) {
}
