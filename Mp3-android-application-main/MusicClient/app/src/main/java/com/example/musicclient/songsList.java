package com.example.musicclient;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import static com.example.musicclient.MainActivity.songList;

public class songsList extends AppCompatActivity {

    RVClickListener rvClickListener;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        rvClickListener = new RVClickListener(){

            @Override
            public void onClick(View v, int position) {

                    try{
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                    try {
                        mediaPlayer.setDataSource(songList.get(position).getSongUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
            }
        };

        setLayout();
    }

    private void setLayout() {
        myAdapter customAdaptor= new myAdapter(this, songList, rvClickListener);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        registerForContextMenu(recyclerView);
        recyclerView.setAdapter(customAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStop() {

        super.onStop();
        try{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}