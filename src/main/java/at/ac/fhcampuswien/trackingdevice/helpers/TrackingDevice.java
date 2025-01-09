package at.ac.fhcampuswien.trackingdevice.helpers;

import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;

// DO NOT CHANGE THIS CLASS!
public class TrackingDevice {
    private static final String RIDDLEID = "2";

    private final String secret;

    public TrackingDevice(String code) {
        this.secret = code;  // secret code to start tracking device
    }

    public void boot(ApiResponseHandler apiResponseHandler) {
        System.out.println("Tracking device is ready to boot");
        checkIsSolved(this.secret, apiResponseHandler);
    }

    public void checkIsSolved(String secret, ApiResponseHandler apiResponseHandler) {
        RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);
        riddleSceneBaseController.submit(secret, apiResponseHandler);
    }
}
