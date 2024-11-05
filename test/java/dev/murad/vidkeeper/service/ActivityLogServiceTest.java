package dev.murad.vidkeeper.service;

import dev.murad.vidkeeper.controller.dto.ActivityLogDTO;
import dev.murad.vidkeeper.domain.Action;
import dev.murad.vidkeeper.domain.ActivityLog;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.repository.ActivityLogRepository;
import dev.murad.vidkeeper.repository.UserRepository;
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

class ActivityLogServiceTest {

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityLogService activityLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveActivityLog() {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setAction(Action.UPDATED);

        when(activityLogRepository.save(activityLog)).thenReturn(activityLog);

        ActivityLog savedActivityLog = activityLogRepository.save(activityLog);

        assertNotNull(savedActivityLog);
        assertEquals(Action.UPDATED, savedActivityLog.getAction());
    }


}