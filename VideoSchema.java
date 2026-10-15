package com.app.db;

public class VideoSchema {
    private String videoName;
    private String videoID;
    private String thumbnail;
    private String viewCount;
    private String channelName;
    private String videoDuration;

    public VideoSchema() {}

    public VideoSchema(String videoName, String videoID, String thumbnail, String viewCount, String channelName, String videoDuration) {
        this.videoName = videoName;
        this.videoID = videoID;
        this.thumbnail = thumbnail;
        this.viewCount = viewCount;
        this.channelName = channelName;
        this.videoDuration = videoDuration;
    }

    public String getVideoName() { return (videoName != null) ? videoName : "No Title"; }
    public void setVideoName(String videoName) { this.videoName = videoName; }

    public String getVideoID() { return (videoID != null) ? videoID : ""; }
    public void setVideoID(String videoID) { this.videoID = videoID; }

    public String getThumbnail() { return (thumbnail != null) ? thumbnail : ""; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getViewCount() { return (viewCount != null) ? viewCount : "0"; }
    public void setViewCount(String viewCount) { this.viewCount = viewCount; }

    public String getChannelName() { return (channelName != null) ? channelName : "YouTube Channel"; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public String getVideoDuration() { return (videoDuration != null) ? videoDuration : ""; }
    public void setVideoDuration(String videoDuration) { this.videoDuration = videoDuration; }
}