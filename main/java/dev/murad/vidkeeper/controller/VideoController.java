package dev.murad.vidkeeper.controller;

import dev.murad.vidkeeper.controller.dto.UserVideoAssignmentDTO;
import dev.murad.vidkeeper.controller.dto.VideoResponseDTO;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.domain.Video;
import dev.murad.vidkeeper.service.UserService;
import dev.murad.vidkeeper.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final UserService userService;

    public VideoController(VideoService videoService, UserService userService) {
        this.videoService = videoService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VideoResponseDTO uploadVideo(@RequestParam("title") String title,
                                        @RequestParam("description") String description,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Uploading video: {}", title);
        String videoUrl = videoService.storeFile(file);
        Video video = Video.builder()
                .title(title)
                .description(description)
                .videoUrl(videoUrl)
                .build();
        Video savedVideo = videoService.saveVideo(video);
        return convertToResponseDTO(savedVideo);
    }

    @GetMapping
    public List<VideoResponseDTO> getAllVideos() {
        logger.info("Fetching all videos");
        return videoService.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VideoResponseDTO updateVideo(@PathVariable Long id,
                                        @RequestParam("title") String title,
                                        @RequestParam("description") String description,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Updating video with ID: {}", id);
        Video existingVideo = videoService.findById(id).orElseThrow();
        String videoUrl = videoService.storeFile(file);
        existingVideo.setTitle(title);
        existingVideo.setDescription(description);
        existingVideo.setVideoUrl(videoUrl);
        Video updatedVideo = videoService.saveVideo(existingVideo);
        return convertToResponseDTO(updatedVideo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVideo(@PathVariable Long id) {
        logger.info("Deleting video with ID: {}", id);
        videoService.deleteById(id);
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public void assignVideoToUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Assigning video with ID: {} to user: {}", id, user.getUsername());
        Video video = videoService.findById(id).orElseThrow();
        User existingUser = userService.findById(user.getId());
        videoService.assignVideoToUser(video, existingUser);
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('USER')")
    public Set<VideoResponseDTO> getAssignedVideos(@PathVariable String username) {
        logger.info("Fetching assigned videos for user ID: {}", username);
        User user = userService.findByUserName(username);
        List<Video> assignedVideos = videoService.findByAssignedToUser(user);
        return assignedVideos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toSet());
    }


    @GetMapping("/all-assigned-videos")
    public ResponseEntity<List<UserVideoAssignmentDTO>> getAllAssignedVideos() {
        List<UserVideoAssignmentDTO> allAssignedVideos = videoService.getAllAssignedVideos().stream()
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(allAssignedVideos);
    }

    private VideoResponseDTO convertToResponseDTO(Video video) {
        return VideoResponseDTO.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .videoUrl(video.getVideoUrl())
                .build();
    }
}