package dev.murad.vidkeeper.controller.dto;

import dev.murad.vidkeeper.domain.Action;

import java.time.LocalDateTime;

public record ActivityLogDTO(String username, String videoTitle, String action, LocalDateTime timestamp) {}