// IMyAidlInterface.aidl
package com.example.AIDLFiles;
import com.example.AIDLFiles.Song;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Song> getAllSongs();
    Song getSpecifiedSong(int songID);
    String getSpecifiedSongURL(int songID);
}