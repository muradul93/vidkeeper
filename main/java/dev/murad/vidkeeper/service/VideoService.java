package dev.murad.vidkeeper.service;

import dev.murad.vidkeeper.controller.dto.UserVideoAssignmentDTO;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.domain.UserVideoAssignment;
import dev.murad.vidkeeper.domain.Video;
import dev.murad.vidkeeper.repository.UserVideoAssignmentRepository;
import dev.murad.vidkeeper.repository.VideoRepository;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserVideoAssignmentRepository userVideoAssignmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private final Path fileStorageLocation;
    private final ServletContext servletContext;

    public VideoService(VideoRepository videoRepository, UserVideoAssignmentRepository userVideoAssignmentRepository, ServletContext servletContext) {
        this.videoRepository = videoRepository;
        this.userVideoAssignmentRepository = userVideoAssignmentRepository;
        this.servletContext = servletContext;
        this.fileStorageLocation = Paths.get("src/main/resources/static/videos").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Video saveVideo(Video video) {
        logger.info("Saving video: {}", video.getTitle());
        return videoRepository.save(video);
    }

    public List<Video> findAll() {
        logger.info("Fetching all videos");
        return videoRepository.findAll();
    }

    public Optional<Video> findById(Long id) {
        logger.info("Finding video by ID: {}", id);
        return videoRepository.findById(id);
    }

    public void deleteById(Long id) {
        logger.info("Deleting video by ID: {}", id);
        videoRepository.deleteById(id);
    }

    public void assignVideoToUser(Video video, User user) {
        UserVideoAssignment assignment = UserVideoAssignment.builder()
                .user(user)
                .video(video)
                .build();
        userVideoAssignmentRepository.save(assignment);
    }

    public List<Video> findByAssignedToUser(User user) {
        logger.info("Fetching videos assigned to user: {}", user.getUsername());
        List<Long> videoIds = userVideoAssignmentRepository.findByUser(user).stream()
                .map(assignment -> assignment.getVideo().getId())
                .distinct()
                .collect(Collectors.toList());

        return videoRepository.findAllById(videoIds)
                .stream()
                .collect(Collectors.toList());
    }


    public List<UserVideoAssignmentDTO> getAllAssignedVideos() {
        return userVideoAssignmentRepository.findAll().stream()
                .map(assignment -> new UserVideoAssignmentDTO(
                        assignment.getUser().getId(),
                        assignment.getUser().getUsername(),
                        assignment.getVideo().getId(),
                        assignment.getVideo().getTitle()
                ))
                .sorted(Comparator.comparing(UserVideoAssignmentDTO::getUserName))
                .collect(Collectors.toList());
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return "/static/videos/" + fileName;
    }
}