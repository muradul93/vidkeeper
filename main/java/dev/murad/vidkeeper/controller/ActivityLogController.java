package dev.murad.vidkeeper.controller;

import dev.murad.vidkeeper.controller.dto.ActivityLogDTO;
import dev.murad.vidkeeper.controller.dto.LogUserActionRequest;
import dev.murad.vidkeeper.service.ActivityLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-log")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ActivityLogDTO> getAllActivityLogs() {
        return activityLogService.getAllActivityLogs();
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> logUserAction(@RequestBody LogUserActionRequest request) {
        activityLogService.logUserAction(request.username(), request.videoId(), request.action());
        return ResponseEntity.ok("User action logged successfully");
    }


}