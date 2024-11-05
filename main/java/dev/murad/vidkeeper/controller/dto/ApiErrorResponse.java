package dev.murad.vidkeeper.controller.dto;



public record ApiErrorResponse(
    int errorCode,
    String description) {

}
