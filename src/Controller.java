
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {


    @FXML
    private Button playButton, resetButton, previousButton, nextButton;

    @FXML
    private ImageView playImage;


    @FXML
    private Pane pane;

    @FXML
    private Label songLabel, songTimer, songDuration;

    @FXML
    private Slider volumeSlider;

    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    private ProgressBar songProgressBar;

    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber;
    private Timer timer;
    private TimerTask task;
    private boolean running;

    private boolean isPaused = true;

    Image image = new Image(getClass().getResourceAsStream("paus2.png"));
    Image image2 = new Image(getClass().getResourceAsStream("b56a1d3486dc4974fba2b4389c283ea4.png"));










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        songs = new ArrayList<File>();

        directory = new File("music");

        files = directory.listFiles();

        if(files != null)
        {
            for(File file : files)
            {
                songs.add(file);
                System.out.println(file);
            };
        }



              media = new Media(songs.get(songNumber).toURI().toString());
              mediaPlayer = new MediaPlayer(media);
//
               songLabel.setText(songs.get(songNumber).getName());
//
//

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);

            }
        });

//        songDuration.textProperty().bind(
//                Bindings.createStringBinding(() ->{
//                    Duration time = mediaPlayer.getCurrentTime();
//                    return String.format("%4d:%02d:%04.1f");
//                    (int) time.toHours(),
//                            (int) time.toMinutes() % 60,
//                    time.toSeconds() % 3600);
//
//                        },
//                        mediaPlayer.currentTimeProperty()));


//        songDuration.textProperty().bind(
//                Bindings.createStringBinding(() -> {
//                            Duration time = mediaPlayer.getCurrentTime();
//                            return String.format("%4d:%02d:%04.1f",
//                                    (int) time.toHours(),
//                                    (int) time.toMinutes() % 60,
//                                    time.toSeconds() % 3600);
//                        },
//                        mediaPlayer.currentTimeProperty()));


        songProgressBar.setStyle("-fx-accent: violet");




    }


    public void playMedia()
    {
        if(isPaused) {
            isPaused = false;
            beginTimer();
            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            mediaPlayer.play();
            playImage.setImage(image);


        }else if (!isPaused)
        {
            isPaused = true;
            cancelTimer();
            mediaPlayer.pause();
            playImage.setImage(image2);

        }



    }

    public void resetMedia()
    {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));

    }

    public void previousMedia()
    {
        if(songNumber > 0)
        {
            songNumber--;

            mediaPlayer.stop();
            playImage.setImage(image2);

            if(running)
            {
                cancelTimer();
                songProgressBar.setProgress(0);
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

        }
        else{
            songNumber = songs.size() - 1;

            mediaPlayer.stop();
            playImage.setImage(image2);
            if(running)
            {
                cancelTimer();
                songProgressBar.setProgress(0);
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
        }

    }

    public void nextMedia()
    {
        if(songNumber < songs.size() - 1)
        {
            songNumber++;

            mediaPlayer.stop();
            playImage.setImage(image2);
            if(running)
            {
                cancelTimer();
                songProgressBar.setProgress(0);
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

        }
        else{
            songNumber = 0;

            mediaPlayer.stop();
            playImage.setImage(image2);
            if(running)
            {
                cancelTimer();
                songProgressBar.setProgress(0);
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
        }

    }

    public void beginTimer()
    {

        timer = new Timer();
        task = new TimerTask()
        {

            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();

                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current / end);

                if(current/end == 1)
                {
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);


    }

    public void cancelTimer()
    {
        running = false;
        timer.cancel();

    }




}
