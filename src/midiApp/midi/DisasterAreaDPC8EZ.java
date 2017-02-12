package midiApp.midi;

import midiApp.common.SongData;

import javax.sound.midi.*;

public class DisasterAreaDPC8EZ {
    private Receiver r;
    // -1 means unknown, 0 means off, 1 means on
    private int[] currentLoops = {-1, -1, -1, -1, -1, -1, -1, -1};

    public DisasterAreaDPC8EZ(Receiver receiver) {
        r = receiver;
    }

    public void saveSong(SongData song, int songNum) {
        for (int i = 0; i < 4; i++) {
            int preset = songNum * 4 + i + 1;
            changeSettings(song.getLoops()[i]);
            saveCurrentLoopsToPreset(preset);
        }
    }

    public void changeSettings(int[] loops) {
        for (int i = 0; i < loops.length; i++) {
            if (currentLoops[i] != loops[i]) {
                currentLoops[i] = loops[i];
                if (loops[i] == 1)
                    engage(i + 1);
                else
                    bypass(i + 1);
            }
        }
    }

    public boolean saveCurrentLoopsToPreset(int preset) {
        try {
            Thread.sleep(200);
            System.out.println("Saving preset " + preset);
            send(MidiUtils.ccChange(122, preset));
            Thread.sleep(1500);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public boolean engage(int loop) {
        try {
            Thread.sleep(200);
            send(MidiUtils.ccChange(loop + 49, 100));
            Thread.sleep(300);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public boolean bypass(int loop) {
        try {
            Thread.sleep(200);
            send(MidiUtils.ccChange(loop + 49, 0));
            Thread.sleep(300);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public void send(MidiMessage midiMessage) {
        r.send(midiMessage, -1);
    }
}