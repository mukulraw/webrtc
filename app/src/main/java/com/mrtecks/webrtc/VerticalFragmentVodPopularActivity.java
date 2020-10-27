package com.mrtecks.webrtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class VerticalFragmentVodPopularActivity extends AppCompatActivity {

    List<videoTypes> list2 = new ArrayList<>();
    CustomViewPager pager;

    int pos;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_fragment_vod_popular);

        toolbar = findViewById(R.id.toolbar);
        bean b = (bean) getApplicationContext();

        list2 = b.plist;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();
            }
        });

        pos = Integer.parseInt(getIntent().getStringExtra("position"));

        pager = findViewById(R.id.pager);


        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT , list2);
        pager.setAdapter(adapter);

        pager.setCurrentItem(pos);

    }
    class PagerAdapter extends FragmentStatePagerAdapter {

        List<videoTypes> ll = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, List<videoTypes> ll) {
            super(fm, behavior);
            this.ll = ll;
        }


        @Override
        public Fragment getItem(int position) {

            VideoPlayerFragmentvod frag = new VideoPlayerFragmentvod();
            Bundle b = new Bundle();
            b.putString("uri", ll.get(position).getVideo());
            b.putString("videoId", ll.get(position).getVideo());
            b.putString("userId", ll.get(position).getUsername());
            b.putString("thumburl", ll.get(position).getUser_image());
            //   b.putString("pic", b1.BASE_URL + ll.get(position).getUserImage());
            frag.setArguments(b);
            return frag;


        }

        @Override
        public int getCount() {
            return ll.size();
            //ll.size();
        }
    }

}