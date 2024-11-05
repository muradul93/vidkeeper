package dev.murad.vidkeeper.service;

import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.domain.UserVideoAssignment;
import dev.murad.vidkeeper.domain.Video;
import dev.murad.vidkeeper.repository.UserVideoAssignmentRepository;
import dev.murad.vidkeeper.repository.VideoRepository;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UserVideoAssignmentRepository userVideoAssignmentRepository;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveVideo() {
        Video video = new Video();
        video.setTitle("Test Video");

        when(videoRepository.save(video)).thenReturn(video);

        Video savedVideo = videoService.saveVideo(video);

        assertNotNull(savedVideo);
        assertEquals("Test Video", savedVideo.getTitle());
    }

    @Test
    void testFindAll() {
        Video video1 = new Video();
        Video video2 = new Video();
        when(videoRepository.findAll()).thenReturn(Arrays.asList(video1, video2));

        List<Video> videos = videoService.findAll();

        assertEquals(2, videos.size());
    }

    @Test
    void testFindById() {
        Video video = new Video();
        video.setId(1L);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        Optional<Video> foundVideo = videoService.findById(1L);

        assertTrue(foundVideo.isPresent());
        assertEquals(1L, foundVideo.get().getId());
    }

    @Test
    void testDeleteById() {
        doNothing().when(videoRepository).deleteById(1L);

        videoService.deleteById(1L);

        verify(videoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignVideoToUser() {
        Video video = new Video();
        User user = new User();
        UserVideoAssignment assignment = new UserVideoAssignment();
        assignment.setUser(user);
        assignment.setVideo(video);

        when(userVideoAssignmentRepository.save(any(UserVideoAssignment.class))).thenReturn(assignment);

        videoService.assignVideoToUser(video, user);

        verify(userVideoAssignmentRepository, times(1)).save(any(UserVideoAssignment.class));
    }

    @Test
    void testFindByAssignedToUser() {
        User user = new User();
        user.setUsername("testuser");
        UserVideoAssignment assignment = new UserVideoAssignment();
        Video video = new Video();
        video.setId(1L);
        assignment.setVideo(video);
        when(userVideoAssignmentRepository.findByUser(user)).thenReturn(Arrays.asList(assignment));
        when(videoRepository.findAllById(anyList())).thenReturn(Arrays.asList(video));

        List<Video> videos = videoService.findByAssignedToUser(user);

        assertEquals(1, videos.size());
        assertEquals(1L, videos.get(0).getId());
    }
}