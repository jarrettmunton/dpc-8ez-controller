package midiApp.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class FileUtils {
    //    private static String dataFolder = FileUtils.class.getResource("..").getPath() + "Data/";
    private static String dataFolder = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "Data" + File.separator;

    private static String songsFullPath = dataFolder + "presets.in";

    private static String midiFullPath = dataFolder + "midi.in";

    public static boolean writeSongsToFile(List<SongData> songs) {
        try {
            Paths.get(dataFolder).toFile().mkdirs();
            String fileOutput = "";
            for (SongData song : songs) {
                fileOutput += song.fileOutputString() + "\n";
            }
            fileOutput += "ENDEND";
            Files.write(Paths.get(songsFullPath), fileOutput.getBytes());

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<SongData> getSongsFromFile() {
        List<SongData> songs = new ArrayList<>();

        Scanner presets;
        try {
            presets = new Scanner(Paths.get(songsFullPath).toFile());
        } catch (FileNotFoundException e) {
            return songs;
        }
        String title = presets.nextLine();
        while (!title.equals("ENDEND")) {
            title = title.substring(1);
            songs.add(new SongData(title, presets.nextLine(), presets.nextLine()));
            title = presets.nextLine();
        }
        return songs;
    }


    public static boolean writeSavedMidiDevices(List<String> devices) {
        try {
            Paths.get(dataFolder).toFile().mkdirs();
            String fileOutput = "";
            for (String s : devices) {
                fileOutput += s + "\n";
            }
            Files.write(Paths.get(midiFullPath), fileOutput.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<String> getMidiFromFile() {
        List<String> devices = new ArrayList<>();

        Scanner file;
        try {
            file = new Scanner(Paths.get(midiFullPath).toFile());
        } catch (FileNotFoundException e) {
            return devices;
        }
        while (file.hasNext()) devices.add(file.nextLine());
        return devices;
    }

}