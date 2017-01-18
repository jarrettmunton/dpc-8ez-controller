package midiApp.setListPicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import midiApp.common.FileUtils;
import midiApp.common.SongData;
import midiApp.midi.MidiSelectorController;
import midiApp.songEditor.SongEditorController;


import java.io.IOException;
import java.net.URL;

import java.util.*;

public class MainController implements Initializable {
    public TableView<SongData> songs;
    public ListView<SongData> listView;
    public Button editList;
    public Button start;

    ObservableList<SongData> currentSetList = FXCollections.observableArrayList();
    ObservableList<SongData> songList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listView.setItems(currentSetList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        songs.setItems(songList);

        TableColumn<SongData, String> song = new TableColumn<>("Title");
        song.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        TableColumn<SongData, String> artist = new TableColumn<>("Artist");
        artist.setCellValueFactory(
                new PropertyValueFactory<>("artist")
        );

        songs.getColumns().addAll(Arrays.asList(song, artist));
        songList.addAll(FileUtils.getSongsFromFile());

    }

    public void editList(ActionEvent actionEvent) {
        System.out.println("editList");

        try {
            Stage stage;
            Parent root;

            if (actionEvent.getSource() == editList) {
                stage = new Stage();
                URL url = getClass().getResource("../songEditor/songEditor.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(url);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                root = fxmlLoader.load();
                ((SongEditorController)fxmlLoader.getController()).setContext(songList, this);

                stage.setScene(new Scene(root));
                stage.setTitle("bleh");
                stage.initOwner(editList.getScene().getWindow());
                stage.showAndWait();
                stage.setOnCloseRequest(event -> {
                    songs.getColumns().get(0).setVisible(false);
                    songs.getColumns().get(0).setVisible(true);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshColumns(){
        songs.getColumns().get(0).setVisible(false);
        songs.getColumns().get(0).setVisible(true);
    }

    public void start(ActionEvent actionEvent) {
        System.out.println("start");


        try {
            Stage stage;
            Parent root;

            if (actionEvent.getSource() == start) {
                stage = new Stage();
                URL url = getClass().getResource("../midi/midiSelector.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(url);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                root = fxmlLoader.load();
                ((MidiSelectorController)fxmlLoader.getController()).setSongList(currentSetList);

                stage.setScene(new Scene(root));
                stage.setTitle("bleh");
                stage.initOwner(start.getScene().getWindow());
                stage.showAndWait();
                stage.setOnCloseRequest(event -> {
                    System.out.println("refresh");
                    songs.getColumns().get(0).setVisible(false);
                    songs.getColumns().get(0).setVisible(true);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent actionEvent) {
        System.out.println("add");
        ObservableList<SongData> selectedItems = songs.getSelectionModel().getSelectedItems();
        listView.getItems().addAll(selectedItems);
    }

    public void remove(ActionEvent actionEvent) {
        System.out.println("remove");
        ObservableList<SongData> selectedItems = listView.getSelectionModel().getSelectedItems();
        listView.getItems().removeAll(selectedItems);
    }

    public void up(ActionEvent actionEvent) {
        System.out.println("up");
        ObservableList<Integer> selectedIndices = listView.getSelectionModel().getSelectedIndices();
        TreeSet<Integer> selection = new TreeSet<>(Comparator.naturalOrder());
        selection.addAll(selectedIndices);
        int bottomIndex = 0;
        boolean canMove = false;
        listView.getSelectionModel().clearSelection();

        for (int setListIndex : selection) {
            if (!canMove) {
                canMove = (setListIndex != bottomIndex);
                bottomIndex++;
            }
            if (canMove) {
                SongData s = currentSetList.remove(setListIndex);
                currentSetList.add(setListIndex - 1, s);
                listView.getSelectionModel().select(setListIndex - 1);
            }
        }
    }


    public void down(ActionEvent actionEvent) {
        System.out.println("down");
        ObservableList<Integer> selectedIndices = listView.getSelectionModel().getSelectedIndices();
        TreeSet<Integer> selection = new TreeSet<>(Comparator.reverseOrder());
        selection.addAll(selectedIndices);
        int topIndex = currentSetList.size() - 1;
        boolean canMove = false;
        listView.getSelectionModel().clearSelection();

        for (int setListIndex : selection) {
            if (!canMove) {
                canMove = (setListIndex != topIndex);
                topIndex--;
            }
            if (canMove) {
                SongData s = currentSetList.remove(setListIndex);
                currentSetList.add(setListIndex + 1, s);
                listView.getSelectionModel().select(setListIndex + 1);
            }
        }
    }

}