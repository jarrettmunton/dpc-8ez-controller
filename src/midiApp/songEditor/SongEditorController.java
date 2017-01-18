package midiApp.songEditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import midiApp.common.FileUtils;
import midiApp.common.SongData;
import midiApp.setListPicker.MainController;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SongEditorController implements Initializable {
    public TableView<SongData> songs;
    public TextField titleField;
    public TextField artistField;
    public VBox setting0;
    public VBox setting1;
    public VBox setting2;
    public VBox setting3;
    public AnchorPane form;

    VBox[] settings;
    ObservableList<SongData> songList;
    SongData currentSong = null;
    MainController mainController = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<SongData, String> song = new TableColumn<>("Title");
        song.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        TableColumn<SongData, String> artist = new TableColumn<>("Artist");
        artist.setCellValueFactory(
                new PropertyValueFactory<>("artist")
        );
        songs.getColumns().addAll(Arrays.asList(song, artist));
        songs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectionChanged(newSelection);
        });

        settings = new VBox[]{setting0, setting1,setting2,setting3};


        form.setDisable(true);
        int i = 0;
        for(VBox v : settings){
            HBox top = (HBox)v.getChildren().get(0);
            HBox bottom = (HBox)v.getChildren().get(1);
            int j = 7;
            for(Node n : top.getChildren()) {
                n.setId(i+""+j);
                j--;
            }
            for(Node n : bottom.getChildren()) {
                n.setId(i+""+j);
                j--;
            }
            i++;
        }

    }


    public void setContext(ObservableList<SongData> songList, MainController mainController){
        this.songList = songList;
        songs.setItems(songList);
        this.mainController = mainController;
    }



    private void selectionChanged(SongData newSong){
        if(newSong !=null) {
            currentSong = newSong;
            displayCurrentlySelectedSongData();
            form.setDisable(false);
        }
        else form.setDisable(true);
    }

    public void createNewSong(ActionEvent actionEvent){
        System.out.println("createNewSong");
        int[][] presets = new int[4][8];
        currentSong = new SongData(presets, "new song" , "");
        songList.add(currentSong);
        songs.getSelectionModel().select(currentSong);
    }

    private void displayCurrentlySelectedSongData(){
        titleField.setText(currentSong.getTitle());
        artistField.setText(currentSong.getArtist());
        int i = 0;
        for(VBox v : settings){
            HBox top = (HBox)v.getChildren().get(0);
            HBox bottom = (HBox)v.getChildren().get(1);
            int j = 7;
            for(Node n : top.getChildren()) {
                if (currentSong.getLoops()[i][j] == 0)
                    turnCircleOff((Circle) n);
                else turnCircleOn((Circle) n);
                j--;
            }
            for(Node n : bottom.getChildren()) {
                if (currentSong.getLoops()[i][j] == 0)
                    turnCircleOff((Circle) n);
                else turnCircleOn((Circle) n);
                j--;
            }
            i++;
        }

    }

    public void artistChanged(){
        currentSong.setArtist(artistField.getText());
        songs.getColumns().get(0).setVisible(false);
        songs.getColumns().get(0).setVisible(true);
        mainController.refreshColumns();
    }

    public void titleChanged(){
        currentSong.setTitle(titleField.getText());
        songs.setItems(songList);
        songs.getColumns().get(0).setVisible(false);
        songs.getColumns().get(0).setVisible(true);
        mainController.refreshColumns();
    }

    public void save(ActionEvent actionEvent) {
        if(FileUtils.writeSongsToFile(songList)) System.out.println("success");
        else System.out.println("failure");

    }

    private void turnSettingOn(Circle c){
        int id =Integer.parseInt(c.getId());
        currentSong.getEditableLoops()[id/10][id%10] = 1;
        turnCircleOn(c);
    }
    private void turnSettingOff(Circle c){
        int id =Integer.parseInt(c.getId());
        currentSong.getEditableLoops()[id/10][id%10] = 0;
        turnCircleOff(c);
    }
    private void turnCircleOn(Circle c){
        Color color;
        if(c.getStyleClass().contains("topRow"))
            color = Color.GREEN;
        else color = Color.ROYALBLUE;
        c.setFill(color);
    }

    private void turnCircleOff(Circle c){
        c.setFill(Color.GRAY);
    }

    public void toggleSetting(Event event) {
        if (event.getSource()instanceof Circle){
            Circle c = (Circle) event.getSource();
            if(c.getFill().equals(Color.GRAY)){
                turnSettingOn(c);
            }
            else turnSettingOff(c);
//
        }
    }
}