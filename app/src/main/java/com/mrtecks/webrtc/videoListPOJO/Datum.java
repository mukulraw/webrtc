package com.mrtecks.webrtc.videoListPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {
    @SerializedName("tbl_vedio_id")
    @Expose
    private String tblVedioId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("Video_Bitrate")
    @Expose
    private String videoBitrate;
    @SerializedName("Video_Resolution")
    @Expose
    private String videoResolution;
    @SerializedName("video_likes")
    @Expose
    private String videoLikes;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("share")
    @Expose
    private String share;
    @SerializedName("follow")
    @Expose
    private String follow;
    @SerializedName("video_name")
    @Expose
    private String videoName;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("sound_details")
    @Expose
    private List<SoundDetail> soundDetails = null;

    public String getTblVedioId() {
        return tblVedioId;
    }

    public void setTblVedioId(String tblVedioId) {
        this.tblVedioId = tblVedioId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(String videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    public String getVideoLikes() {
        return videoLikes;
    }

    public void setVideoLikes(String videoLikes) {
        this.videoLikes = videoLikes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<SoundDetail> getSoundDetails() {
        return soundDetails;
    }

    public void setSoundDetails(List<SoundDetail> soundDetails) {
        this.soundDetails = soundDetails;
    }

}
