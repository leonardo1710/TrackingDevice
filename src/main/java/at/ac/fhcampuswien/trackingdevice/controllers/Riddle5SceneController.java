package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.helpers.Localizer;
import at.ac.fhcampuswien.trackingdevice.helpers.PortalTracker;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Riddle5SceneController extends BaseController {

    private final String RIDDLEID = "5";
    static public String terminalMessage = "";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ImageView image;

    @FXML
    public TextField timeField;

    @FXML
    public Button submit;

    @FXML
    public Text terminal;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);
    }

    @FXML
    public void submit() {
        riddleSceneBaseController.submit(timeField.getText(), null);
    }


    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> image.setImage(background));

        timeField.setVisible(true);
        timeField.setDisable(true);

        submit.setVisible(true);
        submit.setDisable(true);

        List<Control> controls = new ArrayList<>() {{add(timeField); add(submit);}};

        if (response.getJSONObject("riddle").has("message")) {
            terminalMessage = response.getJSONObject("riddle").get("message").toString();
        } else if (response.has("msg")){
            terminalMessage = ">>" + response.get("msg").toString();
        } else {
            System.err.println("There was a problem retrieving riddle " + RIDDLEID + " from the Backend.");
        }

        timeline = UIUtility.animateTypewriterConstant(terminal, terminalMessage, controls);

        RiddleSceneBaseController.initializeZoomableImageView(image, scrollPane);
    };

    @FXML
    public void skipTypewriterAnimation(){
        // Stop the timeline when the stop button is clicked
        if (timeline != null) {
            timeline.stop();
            // print text
            terminal.setText(terminalMessage);
            // Enable controls after stopping animation
            List<Control> controlsToEnable = List.of(timeField, submit); // Add more controls if needed
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }
}
