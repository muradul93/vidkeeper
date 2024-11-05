package dev.murad.vidkeeper.repository;

import dev.murad.vidkeeper.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {


}
