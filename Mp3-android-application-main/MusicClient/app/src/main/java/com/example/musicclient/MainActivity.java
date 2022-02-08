package com.example.musicclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.AIDLFiles.IMyAidlInterface;
import com.example.AIDLFiles.Song;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean mIsBound = false;
    protected static final String TAG = "MusicClientApp";
    private IMyAidlInterface musicClient;
    public static List<Song> songList;
    public Spinner spinner;
    public Spinner spinner2;
    public MediaPlayer mediaPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bind = findViewById(R.id.button2);
        Button unbind = findViewById(R.id.button);
        Button selectAllSongs = findViewById(R.id.button3);
        Spinner s1 = findViewById(R.id.spinner);
        Spinner s2 = findViewById(R.id.spinner2);
        View img = findViewById(R.id.imageView);
        TextView v1 = findViewById(R.id.textView3);
        TextView v2 = findViewById(R.id.textView4);

        bind.setEnabled(true);
        unbind.setEnabled(false);
        selectAllSongs.setEnabled(false);
        s1.setEnabled(false);
        s1.setClickable(false);
        s2.setEnabled(false);
        s2.setClickable(false);
        v1.setEnabled(false);
        v2.setEnabled(false);

        try{
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }


        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        final Button retriveAllSongInfo = findViewById(R.id.button3);
        retriveAllSongInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Retrive all songs info","Calling AIDL function - 1");
                try {
                    songList = musicClient.getAllSongs();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), songsList.class);
                startActivity(intent);
            }
        });


        //final Button bind = findViewById(R.id.button2);
        bind.setOnClickListener(v -> {
            if(!mIsBound){
                checkBindingAndBind();
            }
            else{
                Log.i(TAG,"Service is already bound");
            }
        });

        // unbind
        unbind.setOnClickListener(v -> {

            bind.setEnabled(true);
            unbind.setEnabled(false);
            selectAllSongs.setEnabled(false);
            s1.setEnabled(false);
            s1.setClickable(false);
            s2.setEnabled(false);
            s2.setClickable(false);
            v1.setEnabled(false);
            v2.setEnabled(false);

            try{
                mediaPlayer1.stop();
                mediaPlayer1.release();
                mediaPlayer1 = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }

            if(mIsBound){
                unbindService(mConnection);
                mIsBound = false;
            }
            else{
                Log.i(TAG,"Service is already bound");
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i < songList.size()){
                    Bitmap iv = songList.get(i).getImageId();
                    String v1 = songList.get(i).getArtistName();
                    String v2 = songList.get(i).getSongName();
                    TextView view1 = findViewById(R.id.textView4);
                    TextView view2 = findViewById(R.id.textView3);
                    view1.setText(v1);
                    view2.setText(v2);
                    ImageView imgview = findViewById(R.id.imageView);
                    imgview.setImageBitmap(iv);
                }
                else{
                    TextView view1 = findViewById(R.id.textView4);
                    TextView view2 = findViewById(R.id.textView3);
                    view1.setText("");
                    view2.setText("");
                    ImageView imgview = findViewById(R.id.imageView);
                    imgview.setImageBitmap(null);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position < songList.size()){
                    try {
                        mediaPlayer1.stop();
                        mediaPlayer1.reset();
                        mediaPlayer1.release();
                        mediaPlayer1 = null;
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    mediaPlayer1 = new MediaPlayer();

                    mediaPlayer1.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );

                    try {
                        mediaPlayer1.setDataSource(songList.get(position).getSongUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        mediaPlayer1.prepare(); // might take long! (for buffering, etc)
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer1.start();
                }
                else{
                    try{
                        mediaPlayer1.stop();
                        mediaPlayer1.release();
                        mediaPlayer1 = null;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }


    protected void checkBindingAndBind() {
        if (!mIsBound) {

            boolean b = false;
            Intent i = new Intent(IMyAidlInterface.class.getName());

            // UB:  Stoooopid Android API-21 no longer supports implicit intents
            // to bind to a service #@%^!@..&**!@
            // Must make intent explicit or lower target API level to 20.
            ResolveInfo info = getPackageManager().resolveService(i, 0);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "Ugo says bindService() succeeded!");
            } else {
                Log.i(TAG, "Ugo says bindService() failed!");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        try{
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Unbind from KeyGenerator Service
    @Override
    protected void onDestroy() {

        super.onDestroy();
        super.onPause();

        if (mIsBound) {
            unbindService(this.mConnection);
            mIsBound = false;
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            Button bind = findViewById(R.id.button2);
            Button unbind = findViewById(R.id.button);
            Button selectAllSongs = findViewById(R.id.button3);
            Spinner s1 = findViewById(R.id.spinner);
            Spinner s2 = findViewById(R.id.spinner2);
            View img = findViewById(R.id.imageView);
            TextView v1 = findViewById(R.id.textView3);
            TextView v2 = findViewById(R.id.textView4);

            bind.setEnabled(false);
            unbind.setEnabled(true);
            selectAllSongs.setEnabled(true);
            s1.setEnabled(true);
            s1.setClickable(true);
            s2.setEnabled(true);
            s2.setClickable(true);
            v1.setEnabled(true);
            v2.setEnabled(true);

            try{
                mediaPlayer1.stop();
                mediaPlayer1.release();
                mediaPlayer1 = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }

            musicClient = IMyAidlInterface.Stub.asInterface(iservice);

            Log.i(TAG, "The service was bound!");

            try {
                songList = musicClient.getAllSongs();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // Play a Song

            String[] arraySpinner = new String[songList.size()+1];

            // Show a Song

            Log.i(TAG, "onClick: "+songList.size());
            int i =0;
            for (i = 0; i < songList.size(); i++){
                arraySpinner[i] = songList.get(i).getSongName();
            }
            arraySpinner[i] = "Select a song";

            // Play a Song
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, arraySpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(songList.size(),false);

            // Show a Song
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, arraySpinner);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setSelection(songList.size(),false);

            mIsBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {

            Button bind = findViewById(R.id.button2);
            Button unbind = findViewById(R.id.button);
            Button selectAllSongs = findViewById(R.id.button3);
            Spinner s1 = findViewById(R.id.spinner);
            Spinner s2 = findViewById(R.id.spinner2);
            View img = findViewById(R.id.imageView);
            TextView v1 = findViewById(R.id.textView3);
            TextView v2 = findViewById(R.id.textView4);

            bind.setEnabled(true);
            unbind.setEnabled(false);
            selectAllSongs.setEnabled(false);
            s1.setEnabled(false);
            s1.setClickable(false);
            s2.setEnabled(false);
            s2.setClickable(false);
            v1.setEnabled(false);
            v2.setEnabled(false);

            try{
                mediaPlayer1.stop();
                mediaPlayer1.release();
                mediaPlayer1 = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }

            musicClient = null;

            mIsBound = false;

        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}