package dev.murad.vidkeeper.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(



    @NotBlank(message = "username cannot be blank")
    String username,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    String password,

    @NotBlank(message = "ROLE cannot be blank")
    String role

   ) {

}
