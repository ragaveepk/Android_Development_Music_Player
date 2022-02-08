package com.example.musicServer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.AIDLFiles.IMyAidlInterface;
import com.example.AIDLFiles.Song;
import java.util.List;

import static com.example.musicServer.MainActivity.songList;


public class MusicServerApplication extends Service {

    private static String CHANNEL_ID = "Music player style" ;
    private static final int NOTIFICATION_ID = 1;
    private Notification notification ;

    @Override
    public void onCreate(){
        super.onCreate();
        this.createNotificationChannel();

        // Create a notification area notification so the user
        // can get back to the MusicServiceClient

        final Intent notificationIntent = new Intent(getApplicationContext(),
                MusicServerApplication.class);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0) ;

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setContentText("Click to Access Music Player")
                .setTicker("Music is playing!")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_background, "Show service", pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Music player notification";
        String description = "The channel for music player notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(description);
        }
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        // Don't automatically restart this Service if it is killed
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {

        public List<Song> getAllSongs() throws RemoteException {
            return songList;
        }

        public Song getSpecifiedSong(int songID){
            return songList.get(songID);
        }

        public String getSpecifiedSongURL(int songID){
            return songList.get(songID).getSongUrl();
        }
    };
}
