package com.mrtecks.webrtc;


import com.mrtecks.webrtc.videoListPOJO.videoListBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @GET("rest-api/stream/find_all")
    Call<List<streamBean>> getStreams();

    @POST("api/video_list_api.php")
    Call<String> getVideos();

}
