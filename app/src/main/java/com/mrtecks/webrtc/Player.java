package com.mrtecks.webrtc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.flashphoner.fpwcsapi.Flashphoner;
import com.flashphoner.fpwcsapi.bean.Connection;
import com.flashphoner.fpwcsapi.bean.Data;
import com.flashphoner.fpwcsapi.bean.StreamStatus;
import com.flashphoner.fpwcsapi.layout.PercentFrameLayout;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.flashphoner.fpwcsapi.session.StreamStatusEvent;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.net.URI;
import java.net.URISyntaxException;

public class Player extends AppCompatActivity {

    String stream;

    private Session session;

    private Stream playStream;

    private SurfaceViewRenderer remoteRender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        stream = getIntent().getStringExtra("stream");

        Flashphoner.init(this);

        Flashphoner.getAudioManager().setUseSpeakerPhone(true);

        remoteRender = findViewById(R.id.remote_video_view);
        PercentFrameLayout remoteRenderLayout = findViewById(R.id.remote_video_layout);

        remoteRenderLayout.setPosition(0, 0, 100, 100);
        remoteRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        remoteRender.setMirror(false);
        remoteRender.requestLayout();

        URI u = null;
        try {
            u = new URI("ws://15.207.243.49:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = null;
        if (u != null) {
            url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();
        }


        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setRemoteRenderer(remoteRender);


        session = Flashphoner.createSession(sessionOptions);


        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        StreamOptions streamOptions = new StreamOptions(stream);

                        playStream = session.createStream(streamOptions);

                        playStream.on(new StreamStatusEvent() {
                            @Override
                            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                                        if (StreamStatus.PLAYING.equals(streamStatus)) {


                                        } else if (StreamStatus.NOT_ENOUGH_BANDWIDTH.equals(streamStatus)) {
                                            Log.d("ssttaattuuss", "Not enough bandwidth stream " + stream.getName() + ", consider using lower video resolution or bitrate. " +
                                                    "Bandwidth " + (Math.round(stream.getNetworkBandwidth() / 1000)) + " " +
                                                    "bitrate " + (Math.round(stream.getRemoteBitrate() / 1000)));
                                        } else if (StreamStatus.FAILED.equals(streamStatus)) {
                                            //onEror(loadingpic);
                                        }


                                    }
                                });
                            }
                        });

                        playStream.play();

                    }
                });
            }

            @Override
            public void onRegistered(Connection connection) {

            }

            @Override
            public void onDisconnection(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        session.connect(new Connection());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (session != null) {
            session.disconnect();
        }

    }

}