package midiApp.midi;

import midiApp.common.IgnoreSlot;
import midiApp.common.SongData;

import javax.sound.midi.Receiver;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class DisasterAreaDPC8EZSongManager {
    private DisasterAreaDPC8EZInterfacer dpcMidiInterfacer;

    DisasterAreaDPC8EZSongManager(Receiver receiver) {
        dpcMidiInterfacer = new DisasterAreaDPC8EZInterfacer(receiver);
    }

    void saveSongs(List<SongData> orderedList) {
        Map<DpcPreset, List<Integer>> loopInfoToPresetNumberMap = new TreeMap<>();
        int count = 0;
        for (SongData songData : orderedList) {
            addSongDataToMap(songData, count, loopInfoToPresetNumberMap);
            count++;
        }
        savePresets(loopInfoToPresetNumberMap, count * 4);
    }

    private void addSongDataToMap(SongData songData, int songNum, Map<DpcPreset, List<Integer>> map) {
        if (songData instanceof IgnoreSlot)
            return;

        int basePreset = songNum * 4 + 1;
        int[][] loops = songData.getLoops();
        for (int i = 0; i < 4; i++) {
            int preset = basePreset + i;
            addPresetToMap(loops[i], preset, map);
        }
    }

    private void addPresetToMap(int[] arrayRepresentation, int presetNumber, Map<DpcPreset, List<Integer>> map) {
        addPresetToMap(new DpcPreset(arrayRepresentation), presetNumber, map);
    }

    private void addPresetToMap(DpcPreset dpcPreset, int presetNumber, Map<DpcPreset, List<Integer>> map) {
        map.putIfAbsent(dpcPreset, new LinkedList<>());
        map.get(dpcPreset).add(presetNumber);
    }

    private void savePresets(Map<DpcPreset, List<Integer>> presetToPresetNumberListMap, int totalNumPresets) {
        int count = 0;
        for (Map.Entry<DpcPreset, List<Integer>> entry : presetToPresetNumberListMap.entrySet()) {
            dpcMidiInterfacer.changeSettings(entry.getKey().getArrayRepresentation());
            System.out.println("Loops set to: " + entry.getKey());
            for (Integer preset : entry.getValue()) {
                System.out.println("Saving preset " + preset + "  (" + (++count) + "/" + totalNumPresets + ")");
                dpcMidiInterfacer.saveCurrentLoopsToPreset(preset);
            }
        }
    }
}