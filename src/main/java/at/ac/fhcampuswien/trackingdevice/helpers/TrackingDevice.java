package at.ac.fhcampuswien.trackingdevice.helpers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.SceneLoader;
import javafx.application.Platform;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class TrackingDevice {
    private static final String RIDDLEID = "2";

    private final String secret;

    public TrackingDevice(String code) {
        this.secret = code;  // secret code to start tracking device
    }

    public void boot() {
        System.out.println("Tracking device is ready to boot");
        checkIsSolved(this.secret);
    }

    // DO NOT CHANGE THIS METHOD
    public void checkIsSolved(String secret) {
        RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);
        riddleSceneBaseController.submit(secret, null);
    }
}
