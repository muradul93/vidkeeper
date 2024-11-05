package dev.murad.vidkeeper.controller.dto;

import lombok.Builder;

@Builder
public record VideoResponseDTO(Long id, String title, String description, String videoUrl, String assignedToUser) {}