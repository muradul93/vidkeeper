package dev.murad.vidkeeper.repository;

import dev.murad.vidkeeper.domain.ActivityLog;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Optional<ActivityLog> findByUserIdAndVideoId(User user, Video video);
}