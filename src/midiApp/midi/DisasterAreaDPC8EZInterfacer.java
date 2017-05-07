package midiApp.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

class DisasterAreaDPC8EZInterfacer {
    private Receiver r;

    // -1 means unknown, 0 means off, 1 means on
    private int[] currentLoops = {-1, -1, -1, -1, -1, -1, -1, -1};

    DisasterAreaDPC8EZInterfacer(Receiver receiver) {
        r = receiver;
    }

    void changeSettings(int[] loops) {
        for (int i = 0; i < loops.length; i++) {
            int loop = loops[i];
            if (currentLoops[i] != loop) {
                currentLoops[i] = loop;
                changeLoopState(i + 1, loop == 1);
            }
        }
    }

    boolean saveCurrentLoopsToPreset(int preset) {
        try {
            Thread.sleep(200);
            send(MidiUtils.ccChange(122, preset));
            Thread.sleep(1500);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    private void changeLoopState(int loop, boolean trueIfOn) {
        while (!tryChangeLoopState(loop, trueIfOn)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tryChangeLoopState(int loop, boolean trueIfOn) {
        int ccValue = trueIfOn ? 100 : 0;
        try {
            Thread.sleep(200);
            send(MidiUtils.ccChange(loop + 49, ccValue));
            Thread.sleep(300);
            return true;
        } catch (InvalidMidiDataException | InterruptedException e) {
            return false;
        }
    }

    private void send(MidiMessage midiMessage) {
        r.send(midiMessage, -1);
    }
}