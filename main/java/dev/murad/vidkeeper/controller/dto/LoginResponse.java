package dev.murad.vidkeeper.controller.dto;



public record LoginResponse(

    String username,
    String role,
    String token) {

}
