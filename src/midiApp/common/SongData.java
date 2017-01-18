package midiApp.common;

import java.util.Arrays;

public class SongData {
    private int[][] loops = new int[4][8];
    private String title;
    private String artist;

    public SongData(String title, String artist, String presets) {
        this.title = title;
        this.artist = artist;
        setLoopInfo(presets);
    }
    public SongData(int[][] presets, String title, String artist) {
        this.title = title;
        this.artist = artist;
        setLoopInfo(presets);
    }

    private void setLoopInfo(String presets) {
        String[] s = presets.split(" ");
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 8; i++)
                loops[j][i] = s[i+8*j].equals("0") ? 0 : 1;
        }
    }

    private boolean setLoopInfo(int[][] presets) {
        if (presets.length != 4 || presets[0].length != 8) return false;
        this.loops = Arrays.copyOf(presets, 8);
        return true;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int[][] getLoops() {
        return Arrays.copyOf(loops, 8);
    }

    public int[][] getEditableLoops() {
        return loops;
    }

    public String fileOutputString() {
        // $ starts each song, so that the EOF indicator of ENDEND can never be hit as a song title
        String s = "$" + title + "\n" + artist + "\n";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                s += loops[i][j];
                s += " ";
            }
        }
        return s.trim();
    }

    public SongData copy(){
        return new SongData(loops.clone(), title, artist);
    }

    public String toString(){
        return title +" - "+artist;
    }
}
