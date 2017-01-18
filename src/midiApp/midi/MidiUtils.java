package midiApp.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class MidiUtils {
    public static ShortMessage ccChange(int ccVal, int value) throws InvalidMidiDataException {
        ShortMessage m = new ShortMessage();
        m.setMessage(ShortMessage.CONTROL_CHANGE, ccVal, value);
        return m;
    }

    public static ShortMessage programChange(int value) throws InvalidMidiDataException {
        ShortMessage m = new ShortMessage();
        m.setMessage(ShortMessage.PROGRAM_CHANGE, value, 0);
        return m;
    }
}
