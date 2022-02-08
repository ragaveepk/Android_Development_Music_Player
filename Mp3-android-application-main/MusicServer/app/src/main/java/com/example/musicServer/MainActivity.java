package com.example.musicServer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.AIDLFiles.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSong("Scientist",
                "Coldplay",
                "https://www.free-stock-music.com/music/mixaund-inspiring-happy-morning.mp3",
                BitmapFactory.decodeResource(getResources(), R.drawable.cp1));
        addSong("Paradise",
                "Coldplay",
                "https://www.free-stock-music.com/music/fsm-team-escp-yellowtree-melancholia-goth-emo-type-beat.mp3",
                BitmapFactory.decodeResource(getResources(), R.drawable.cp2));
        addSong("Every teardrop is a waterfall",
                "Coldplay",
                "https://www.free-stock-music.com/music/deoxys-beats-fushiguro.mp3",
                BitmapFactory.decodeResource(getResources(), R.drawable.cp3));
        addSong("Charlie brown",
                "Coldplay",
                "https://www.free-stock-music.com/music/purrple-cat-snooze-button.mp3",
                BitmapFactory.decodeResource(getResources(), R.drawable.cp4));
        addSong("Magic",
                "Coldplay",
                "https://www.free-stock-music.com/music/liqwyd-coral.mp3",
                BitmapFactory.decodeResource(getResources(), R.drawable.cp5));

    }

    private static void addSong(String songName, String artistName,
                                String youtubeUrl, Bitmap imageId) {

        songList.add(new Song(songName, artistName, youtubeUrl, imageId));

    }
}