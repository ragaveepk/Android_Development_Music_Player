package com.example.AIDLFiles;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

// a song object that models the song requirements mentioned
public class Song implements Parcelable {
    private final String songName;
    private final String artistName;
    private final String songUrl;
    private final Bitmap imageId;

    public String getSongName() { return songName; }

    public String getArtistName() { return artistName; }

    public String getSongUrl () { return songUrl; }

    public Bitmap getImageId () { return imageId; }

    public Song(String songName, String artistName, String youtubeUrl, Bitmap imageId) {
        this.songName = songName;
        this.artistName = artistName;
        this.songUrl = youtubeUrl;
        this.imageId = imageId;
    }

    protected Song(Parcel in) {
        songName = in.readString();
        artistName = in.readString();
        songUrl = in.readString();
        imageId = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeString(artistName);
        dest.writeString(songUrl);
        dest.writeParcelable(imageId, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}