package midiApp.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class MidiUtils {
    public static ShortMessage ccChange(int ccVal, int value) throws InvalidMidiDataException {
        ShortMessage m = new ShortMessage();
        m.setMessage(ShortMessage.CONTROL_CHANGE, 0, ccVal, value);
        return m;
    }

    public static ShortMessage programChange(int value) throws InvalidMidiDataException {
        ShortMessage m = new ShortMessage();
        m.setMessage(ShortMessage.PROGRAM_CHANGE, 0, value, 0);
        return m;
    }

    public static ShortMessage programChange(int value, int channel) throws InvalidMidiDataException {
        ShortMessage m = new ShortMessage();
        m.setMessage(ShortMessage.PROGRAM_CHANGE, channel, value, 0);
        return m;
    }
}
