package com.example.entity;

//视频数据库实体类
public class VideoUpload {
    private Integer id;
    private String videoName;
    private String videoUrl;
    private String videoUUID;
    private String videoPicture;

    public String getVideoPicture() {
        return videoPicture;
    }

    public void setVideoPicture(String videoPicture) {
        this.videoPicture = videoPicture;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    private String courseName;
    private String content;
    private Integer tid;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public VideoUpload(Integer id, String videoName, String videoUrl, String videoUUID, String videoPicture, String courseName, Integer tid) {
        this.id = id;
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.videoUUID = videoUUID;
        this.videoPicture = videoPicture;
        this.courseName = courseName;
        this.tid = tid;
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

