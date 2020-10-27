package com.mrtecks.webrtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.flashphoner.fpwcsapi.FPSurfaceViewRenderer;
import com.flashphoner.fpwcsapi.Flashphoner;
import com.flashphoner.fpwcsapi.bean.Connection;
import com.flashphoner.fpwcsapi.bean.Data;
import com.flashphoner.fpwcsapi.bean.StreamStatus;
import com.flashphoner.fpwcsapi.constraints.AudioConstraints;
import com.flashphoner.fpwcsapi.constraints.Constraints;
import com.flashphoner.fpwcsapi.constraints.VideoConstraints;
import com.flashphoner.fpwcsapi.handler.CameraSwitchHandler;
import com.flashphoner.fpwcsapi.layout.PercentFrameLayout;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.flashphoner.fpwcsapi.session.StreamStatusEvent;
import com.flashphoner.fpwcsapi.webrtc.MediaDevice;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.net.URI;
import java.net.URISyntaxException;

public class GoLive extends AppCompatActivity {

    private Session session;

    private Stream publishStream;

    private FPSurfaceViewRenderer localRender;

    boolean torchStatus = false;

    private static final int PUBLISH_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live);

        Flashphoner.init(this);

        Flashphoner.getAudioManager().setUseSpeakerPhone(true);
        localRender = findViewById(R.id.local_video_view);

        PercentFrameLayout localRenderLayout = findViewById(R.id.local_video_layout);


        localRender.setZOrderMediaOverlay(true);

        localRenderLayout.setPosition(0, 0, 100, 100);
        localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localRender.setMirror(true);
        localRender.requestLayout();

        startPublish(String.valueOf(System.currentTimeMillis()));

    }

    public void startPublish(final String liveId) {

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
        sessionOptions.setLocalRenderer(localRender);


        session = Flashphoner.createSession(sessionOptions);

        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {


                Log.d("fffffffff", data.toString());


            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        StreamOptions streamOptions = new StreamOptions(liveId);

                        VideoConstraints videoConstraints = new VideoConstraints();
                        //videoConstraints.setCameraId(((MediaDevice)mCameraSpinner.getSpinner().getSelectedItem()).getId());
                        videoConstraints.setVideoFps(60);
                        videoConstraints.setResolution(640, 480);
                        streamOptions.setConstraints(new Constraints(new AudioConstraints(), videoConstraints));

                        publishStream = session.createStream(streamOptions);


                        publishStream.on(new StreamStatusEvent() {

                            @Override
                            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                                Log.d("ssttaattuuss", String.valueOf(streamStatus));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                    }
                                });
                            }

                        });

                        ActivityCompat.requestPermissions(GoLive.this,
                                new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                                PUBLISH_REQUEST_CODE);
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


    public void switchCamera() {

        publishStream.switchCamera(new CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean b) {

            }

            @Override
            public void onCameraSwitchError(String s) {

            }
        });

    }

    public void switchTorch() {
        torchStatus = !torchStatus;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PUBLISH_REQUEST_CODE: {
                if (grantResults.length == 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED) {

                    session.disconnect();

                } else {
                    publishStream.publish();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {

        Log.d("offline", "destroy");


        try {

            if (session != null) {
                session.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onDestroy();

    }

}