package com.example.vlcdemo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements MediaPlayer.EventListener, IVLCVout.Callback, LibVLC.HardwareAccelerationError {

    private VideoView live_emvideoview;
    MediaPlayer mMediaPlayer;
    LibVLC mLibVLC;
    SurfaceView live_vlc_video_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        live_vlc_video_player = findViewById(R.id.live_vlc_video_player);
        live_emvideoview = findViewById(R.id.live_emvideoview);

        setPlayer();
        playEmvideoView("http://lamptest.fieldteam360.com/content/90:0E:B3:06:EE:68/adwatch.mp4");

    }

    void setPlayer(){

        ArrayList<String> options = new ArrayList<>();
        options.add("--aout=opensles");
        options.add("--audio-time-stretch");
        options.add("-vvv");
        mLibVLC = new LibVLC(options);
        mLibVLC.setOnHardwareAccelerationError(this);

        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.setEventListener(this);

        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.setVideoView(live_vlc_video_player);
        vout.addCallback(this);
        vout.attachViews();

    }

    void ReleasePlayer() {

        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        mLibVLC.release();

    }

    void playEmvideoView(String url){

        try {
            live_emvideoview.stopPlayback();
            live_emvideoview.setVideoURI(Uri.parse(url));
            live_emvideoview.setScaleType(ScaleType.FIT_XY);
            live_emvideoview.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    live_emvideoview.start();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {

    }



    @Override
    public void eventHardwareAccelerationError() {

    }

    @Override
    public void onEvent(MediaPlayer.Event event) {

    }


    @Override
    protected void onDestroy() {
        ReleasePlayer();

        live_emvideoview.stopPlayback();
        live_emvideoview.release();
        super.onDestroy();
    }

}
