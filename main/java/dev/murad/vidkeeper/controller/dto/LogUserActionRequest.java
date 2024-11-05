package dev.murad.vidkeeper.controller.dto;

public record LogUserActionRequest(String username, Long videoId, String action) {
}