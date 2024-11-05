package dev.murad.vidkeeper.controller;

import dev.murad.vidkeeper.config.SecurityConfig;
import dev.murad.vidkeeper.service.ActivityLogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ActivityLogController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
})
@ContextConfiguration(classes = {ActivityLogController.class, ActivityLogService.class})
class ActivityLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityLogService activityLogService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllActivityLogs() throws Exception {
        Mockito.when(activityLogService.getAllActivityLogs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/activity-log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


}