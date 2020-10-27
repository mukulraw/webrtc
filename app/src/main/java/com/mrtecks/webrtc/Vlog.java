package com.mrtecks.webrtc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrtecks.webrtc.videoListPOJO.Datum;
import com.mrtecks.webrtc.videoListPOJO.videoListBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Vlog extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager manager;
    PopularAdapter adapter;
    List<videoTypes> list;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlog);

        progress = findViewById(R.id.progress);

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);
        manager = new GridLayoutManager(this, 2);
        adapter = new PopularAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://buddyz.fun/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        final bean b = (bean) getApplicationContext();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<String> call = cr.getVideos();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {



                try {
                    JSONObject object = new JSONObject(response.body());

                    JSONArray data = object.getJSONArray("data");
                    Log.d("success" , String.valueOf(data.length()));

                    List<videoTypes> vlist = new ArrayList<>();

                    for (int i = 0 ; i < data.length() ; i++)
                    {
                        videoTypes item = new videoTypes();
                        String user_image = data.getJSONObject(i).getString("user_image");
                        String username = data.getJSONObject(i).getString("username");
                        String video = data.getJSONObject(i).getString("video");

                        item.setUser_image(user_image);
                        item.setUsername(username);
                        item.setVideo(video);

                        vlist.add(item);
                    }

                    adapter.setGridData(vlist);
                    b.plist = vlist;



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*try {

                } catch (Exception e) {
                    e.printStackTrace();
                }*/


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });


    }

    public static class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

        Context context;
        List<videoTypes> list = new ArrayList<>();


        public PopularAdapter(Context context, List<videoTypes> list) {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<videoTypes> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.popular_list_model, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            //holder.setIsRecyclable(false);
            final videoTypes item = list.get(position);
            holder.title.setText(item.getUsername());

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getUser_image(), holder.profile, options);

            Log.d("image", item.getUser_image());

            loader.displayImage(item.getUser_image(), holder.image, options);


            //Glide.with(context).load(item.getTimelineProfileImage()).into(holder.profile);
            //Glide.with(context).load(item.getVideoURL()).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(context, VerticalFragmentVodPopularActivity.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("videoId", item.getVideo());
                    intent.putExtra("url", item.getVideo());
                    intent.putExtra("thumb", item.getUser_image());
                    intent.putExtra("userName", item.getUsername());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, likes;
            ImageView image;
            ImageView profile;

            public MyViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                likes = itemView.findViewById(R.id.likes);
                image = itemView.findViewById(R.id.image);
                profile = itemView.findViewById(R.id.profile);
            }
        }
    }

}