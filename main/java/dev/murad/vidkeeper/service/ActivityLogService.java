package dev.murad.vidkeeper.service;

import dev.murad.vidkeeper.controller.dto.ActivityLogDTO;
import dev.murad.vidkeeper.domain.Action;
import dev.murad.vidkeeper.domain.ActivityLog;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.domain.Video;
import dev.murad.vidkeeper.domain.UserVideoAssignment;
import dev.murad.vidkeeper.repository.ActivityLogRepository;
import dev.murad.vidkeeper.repository.UserRepository;
import dev.murad.vidkeeper.repository.UserVideoAssignmentRepository;
import dev.murad.vidkeeper.repository.VideoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final UserVideoAssignmentRepository userVideoAssignmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogService.class);

    public ActivityLogService(ActivityLogRepository activityLogRepository, UserRepository userRepository, VideoRepository videoRepository, UserVideoAssignmentRepository userVideoAssignmentRepository) {
        this.activityLogRepository = activityLogRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.userVideoAssignmentRepository = userVideoAssignmentRepository;
    }


    public List<ActivityLogDTO> getAllActivityLogs() {
        List<ActivityLog> logs = activityLogRepository.findAll();
        List<ActivityLogDTO> activityLogDTOs = logs.stream()
                .map(log -> {
                    String username = userRepository.findById(log.getUserId().getId())
                            .map(User::getUsername)
                            .orElse("Unknown User");
                    String videoTitle = videoRepository.findById(log.getVideoId().getId())
                            .map(Video::getTitle)
                            .orElse("Unknown Video");
                    return new ActivityLogDTO(username, videoTitle, log.getAction().name(), log.getTimestamp());
                })
                .collect(Collectors.toList());
        return activityLogDTOs;
    }

    public void logUserAction(String userName, Long videoId, String action) {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        Optional<Video> videoOptional = userVideoAssignmentRepository.findById(videoId).map(UserVideoAssignment::getVideo);

        if (userOptional.isPresent() && videoOptional.isPresent()) {
            User user = userOptional.get();
            Video video = videoOptional.get();

            Optional<ActivityLog> existingLog = activityLogRepository.findByUserIdAndVideoId(user, video);
            ActivityLog log;
            if (existingLog.isPresent()) {
                log = existingLog.get();
                log.setAction(Action.valueOf(action));
                log.setTimestamp(LocalDateTime.now());
            } else {
                log = ActivityLog.builder()
                        .userId(user)
                        .videoId(video)
                        .action(Action.valueOf(action))
                        .timestamp(LocalDateTime.now())
                        .build();
            }
            logger.info("Logging user action: {} for userName: {} on video ID: {}", action, userName, videoId);
            activityLogRepository.save(log);
        } else {
            logger.warn("User or Video not found for IDs: userName={}, videoId={}", userName, videoId);
        }
    }
}