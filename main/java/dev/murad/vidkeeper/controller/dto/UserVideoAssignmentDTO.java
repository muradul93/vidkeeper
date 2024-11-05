
package dev.murad.vidkeeper.controller.dto;

public class UserVideoAssignmentDTO {
    private Long userId;
    private String userName;
    private Long videoId;
    private String videoName;

    public UserVideoAssignmentDTO(Long userId, String userName, Long videoId, String videoName) {
        this.userId = userId;
        this.userName = userName;
        this.videoId = videoId;
        this.videoName = videoName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}