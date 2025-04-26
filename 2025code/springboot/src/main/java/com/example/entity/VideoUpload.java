package com.example.entity;

//视频数据库实体类
public class VideoUpload {
    private Integer id;
    private String videoName;
    private String videoUrl;
    private String videoUUID;

    public VideoUpload(Integer id, String videoName, String videoUrl, String videoUUID) {
        this.id = id;
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.videoUUID = videoUUID;
    }

    public VideoUpload() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUUID() {
        return videoUUID;
    }

    public void setVideoUUID(String videoUUID) {
        this.videoUUID = videoUUID;
    }
}

