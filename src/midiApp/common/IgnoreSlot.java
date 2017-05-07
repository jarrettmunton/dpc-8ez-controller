package midiApp.common;

public class IgnoreSlot extends SongData {
    public IgnoreSlot() {
        super(new int[4][8], "ignore", "ignore");
    }

    @Override
    public String toString() {
        return "IGNORE--DO NOTHING";
    }
}
