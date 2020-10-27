package com.mrtecks.webrtc.videoListPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class videoListBean {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_vedio")
    @Expose
    private String totalVedio;
    @SerializedName("limit_vedio")
    @Expose
    private String limitVedio;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalVedio() {
        return totalVedio;
    }

    public void setTotalVedio(String totalVedio) {
        this.totalVedio = totalVedio;
    }

    public String getLimitVedio() {
        return limitVedio;
    }

    public void setLimitVedio(String limitVedio) {
        this.limitVedio = limitVedio;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
