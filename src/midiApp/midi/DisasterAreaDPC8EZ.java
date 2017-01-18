package midiApp.midi;

import midiApp.common.SongData;

import javax.sound.midi.*;

public class DisasterAreaDPC8EZ {
    private Receiver r;

    public DisasterAreaDPC8EZ(Receiver receiver) {
        r = receiver;
    }

    public void saveSong(SongData song, int songNum) {
        for (int i = 0; i < 4; i++) {
            int preset = songNum * 4 + i + 1;
            switchToPreset(preset);
            changeSettings(song.getLoops()[i]);
            saveCurrentLoopsToPreset(preset);
        }
    }

    public void changeSettings(int[] loops) {
        for (int i = 0; i < loops.length; i++) {
            if (loops[i] == 1) engage(i + 1);
            else bypass(i + 1);
        }
    }

    public boolean switchToPreset(int preset) {
        try {
            send(MidiUtils.programChange(preset));
            Thread.sleep(10);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public boolean saveCurrentLoopsToPreset(int preset) {
        try {
            send(MidiUtils.ccChange(122, preset));
            Thread.sleep(2000);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public boolean engage(int loop) {
        try {
            send(MidiUtils.ccChange(loop + 49, 100));
            Thread.sleep(10);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public boolean bypass(int loop) {
        try {
            send(MidiUtils.ccChange(loop + 49, 0));
            Thread.sleep(10);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    public void send(MidiMessage midiMessage) {
        r.send(midiMessage, -1);
    }
}
