package com.mrtecks.webrtc;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @GET("rest-api/stream/find_all")
    Call<List<streamBean>> getStreams();


}
