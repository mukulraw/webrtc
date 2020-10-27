package com.mrtecks.webrtc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.flashphoner.fpwcsapi.Flashphoner;
import com.flashphoner.fpwcsapi.bean.Connection;
import com.flashphoner.fpwcsapi.bean.Data;
import com.flashphoner.fpwcsapi.bean.StreamStatus;
import com.flashphoner.fpwcsapi.handler.CameraSwitchHandler;
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

public class Web extends Fragment {

    Button startend;

    private Session session;

    private Stream publishStream;

    private SurfaceViewRenderer localRender;

    boolean torchStatus = false;

    private static final int PUBLISH_REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web, container, false);

        startend = view.findViewById(R.id.button);

        Flashphoner.init(getActivity());

        Flashphoner.getAudioManager().setUseSpeakerPhone(true);
        localRender = view.findViewById(R.id.local_video_view);

        PercentFrameLayout localRenderLayout = view.findViewById(R.id.local_video_layout);


        localRender.setZOrderMediaOverlay(true);


        localRenderLayout.setPosition(0, 0, 100, 100);
        localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localRender.setMirror(true);
        localRender.requestLayout();

        startend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPublish("test");

            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {

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

    public void startPublish(final String liveId) {

        URI u = null;
        try {
            u = new URI("ws://65.0.117.139:8080");
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

                StreamOptions streamOptions = new StreamOptions(liveId);

                publishStream = session.createStream(streamOptions);

                publishStream.on(new StreamStatusEvent() {

                    @Override
                    public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                        if (StreamStatus.PUBLISHING.equals(streamStatus)) {

                            /**
                             * The options for the stream to play are set.
                             * The stream name is passed when StreamOptions object is created.
                             */
                            //StreamOptions streamOptions = new StreamOptions(liveId);

                            /**
                             * Stream is created with method Session.createStream().
                             */
                            //playStream = session.createStream(streamOptions);

                            /**
                             * Method Stream.play() is called to start playback of the stream.
                             */
                            //playStream.play();
                        } else {
                            Log.e("TAG", "Can not publish stream " + stream.getName() + " " + streamStatus);
                        }
                    }

                });

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                        PUBLISH_REQUEST_CODE);

            }

            @Override
            public void onRegistered(Connection connection) {

            }

            @Override
            public void onDisconnection(final Connection connection) {

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

}
