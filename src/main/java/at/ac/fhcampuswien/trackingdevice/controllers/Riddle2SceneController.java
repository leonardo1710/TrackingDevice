package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.helpers.BootLoader;
import at.ac.fhcampuswien.trackingdevice.helpers.TrackingDevice;
import at.ac.fhcampuswien.trackingdevice.helpers.TrackingDeviceBootException;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Riddle2SceneController extends BaseController {
    private final String RIDDLEID = "2";

    @FXML
    public Text terminal;

    @FXML
    private Button playBtn;

    private Timeline timeline;

    static public String terminalMessage = "Try booting tracking device...\n" +
            "Try booting tracking device...\n" +
            "Try booting tracking device...\n" +
            "Problems occurred while booting tracking device. Find more information in at/ac/fhcampuswien/trackingdevice/controllers/Riddle2SceneController.bootTrackingDevice() method and at/ac/fhcampuswien/trackingdevice/helpers/BootLoader";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = User.getInstance();
        if(!user.isRegistered()){
            System.out.println("> There seems to be some problem with registration.");
            return;
        }

        try {
            bootTrackingDevice();
        } catch (TrackingDeviceBootException e) {
            writeBootloadingProblemsUI();
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println("> There was a problem reading the mysterious message.");
        }
    }

    private void writeBootloadingProblemsUI() {
        timeline = UIUtility.animateTypewriterConstant(terminal, terminalMessage, new ArrayList<>() {{add(playBtn);}});
    }

    private void bootTrackingDevice() throws TrackingDeviceBootException {
        TrackingDevice trackingDevice = null; // TODO fix missing dependencies here
        BootLoader bootLoader = new BootLoader(trackingDevice);
        bootLoader.load(response -> { writeBootloadingProblemsUI(); });
    }

    @FXML
    public void playAudio(){
        AudioClip buzzer = new AudioClip(getClass().getResource("/files/a6e0690bf959e9935cfef9ab23848dde.wav").toExternalForm());
        buzzer.play();
    }

    @FXML
    public void skipTypewriterAnimation(){
        if (timeline != null) {
            timeline.stop();
            terminal.setText(terminalMessage);
            List<Control> controlsToEnable = List.of(playBtn);
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }

}
