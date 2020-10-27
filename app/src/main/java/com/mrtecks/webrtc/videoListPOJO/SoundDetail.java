package com.mrtecks.webrtc.videoListPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoundDetail {
    @SerializedName("tbl_video_sound_id")
    @Expose
    private String tblVideoSoundId;
    @SerializedName("user_image")
    @Expose
    private String userImage;

    public String getTblVideoSoundId() {
        return tblVideoSoundId;
    }

    public void setTblVideoSoundId(String tblVideoSoundId) {
        this.tblVideoSoundId = tblVideoSoundId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
