package midiApp.midi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import midiApp.common.SongData;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import java.net.URL;
import java.util.*;

public class MidiSelectorController implements Initializable {
    List<SongData> songs;
    public TableView<MidiDevice.Info> midiSelector;

    Map<MidiDevice.Info, MidiDevice> midiOptions = new HashMap<>();
    ObservableList<MidiDevice.Info> midiNames = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                MidiDevice d = MidiSystem.getMidiDevice(infos[i]);
                if (d.getMaxReceivers() != 0) {
                    MidiDevice.Info info = d.getDeviceInfo();
                    midiOptions.put(info, d);
                    midiNames.add(info);
                }
            } catch (MidiUnavailableException e) {
            }
        }

        midiSelector.setItems(midiNames);

        TableColumn<MidiDevice.Info, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        TableColumn<MidiDevice.Info, String> vendor = new TableColumn<>("Vendor");
        vendor.setCellValueFactory(
                new PropertyValueFactory<>("vendor")
        );
        TableColumn<MidiDevice.Info, String> version = new TableColumn<>("Version");
        version.setCellValueFactory(
                new PropertyValueFactory<>("version")
        );

        midiSelector.getColumns().addAll(Arrays.asList(name, vendor, version));

    }


    public void setSongList(List<SongData> songList) {
        this.songs = songList;
    }

    public void start() {
        MidiDevice d = midiOptions.get(midiSelector.getSelectionModel().getSelectedItem());

        if (d.isOpen()) return; // todo: display message

        try {
            d.open();
            Receiver r = d.getReceiver();
            DisasterAreaDPC8EZSongManager dpc = new DisasterAreaDPC8EZSongManager(r);
            dpc.saveSongs(songs);
            r.close();
            d.close();
            System.out.println("Successfully updated"); // todo: display message
        } catch (MidiUnavailableException e) {
            System.out.println("Device unavailable"); // todo: display message
        }
    }

}