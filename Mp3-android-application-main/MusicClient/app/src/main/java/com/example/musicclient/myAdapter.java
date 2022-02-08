package com.example.musicclient;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AIDLFiles.Song;

import java.io.IOException;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<com.example.musicclient.myAdapter.myViewHolder> {

    public List<Song> songs;
    Context cx;
    public static int selectedPosition;
    public boolean musicPlayer = true;
    public MediaPlayer mediaPlayer;
    RVClickListener rvClickListener;

    // sets the context and the data to be used to populate the view
    public myAdapter(Context context, List<Song> objects, RVClickListener rvClickListener) {
        songs = objects;
        cx = context;
        this.rvClickListener = rvClickListener;
    }

    // sets the view holders layout
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(cx);
        View view = inflater.inflate(R.layout.list_items, parent, false);
        myViewHolder viewHolder = new myViewHolder(view, rvClickListener);
        viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvClickListener.onClick(view, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;

    }

    // set's the position for the long press listner based in the adapter's position
    public void setPosition(int position) {
        selectedPosition = position;
    }

    // initialize the view holder's fields with the song's components and define the on click functionality
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songText.setText(song.getSongName());
        holder.artistText.setText(song.getArtistName());
        holder.songImage.setImageBitmap(song.getImageId());
        String url = String.valueOf(Uri.parse(song.getSongUrl())); // your URL here
        //MediaPlayer mediaPlayer = new MediaPlayer();
        }


    // define the size of the list
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView songText, artistText;
        ImageView songImage;
        private RVClickListener listener;
        RelativeLayout parent_layout;

        // define the view of the list component
        public myViewHolder(@NonNull View itemView, RVClickListener rvClickListener) {
            super(itemView);
            songText = itemView.findViewById(R.id.songText);
            artistText = itemView.findViewById(R.id.artistText);
            songImage = itemView.findViewById(R.id.songImage);
            parent_layout = itemView.findViewById(R.id.parentLayout);
        }
    }

}
