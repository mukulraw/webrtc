package com.mrtecks.webrtc;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab, vlog;
    RecyclerView grid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.floatingActionButton);
        vlog = findViewById(R.id.floatingActionButton2);
        grid = findViewById(R.id.grid);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GoLive.class);
                startActivity(intent);

            }
        });

        vlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Vlog.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://65.0.117.139:8081/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<List<streamBean>> call = cr.getStreams();

        call.enqueue(new Callback<List<streamBean>>() {
            @Override
            public void onResponse(Call<List<streamBean>> call, Response<List<streamBean>> response) {
                List<streamBean> filteredStreams = new ArrayList<>();
                try {


                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getStatus().equals("PUBLISHING")) {
                            filteredStreams.add(response.body().get(i));
                        }
                    }

                    GridAdapter adapter = new GridAdapter(MainActivity.this, filteredStreams);
                    GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 1);
                    grid.setAdapter(adapter);
                    grid.setLayoutManager(manager);
                } catch (Exception e) {

                    GridAdapter adapter = new GridAdapter(MainActivity.this, filteredStreams);
                    GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 1);
                    grid.setAdapter(adapter);
                    grid.setLayoutManager(manager);

                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<List<streamBean>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        Context context;
        List<streamBean> list = new ArrayList<>();

        public GridAdapter(Context context, List<streamBean> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.text, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            streamBean item = list.get(position);

            holder.text.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Player.class);
                    intent.putExtra("stream", item.getName());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView text;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);

            }
        }
    }

}