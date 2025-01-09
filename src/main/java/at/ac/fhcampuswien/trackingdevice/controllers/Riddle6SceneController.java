package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Riddle6SceneController extends BaseController {

    private static final String RIDDLEID = "6";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ImageView image;


    @FXML
    private TextField turnsField;

    @FXML
    private Button deactivate;

    @FXML
    private Text terminal;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    private Timeline timeline;

    static public String terminalMessage = "";

    @FXML
    private void deactivatePortal() {
        riddleSceneBaseController.submit(turnsField.getText(), null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> {
            image.setImage(background);
            image.setPreserveRatio(true);
        });

        turnsField.setVisible(true);
        turnsField.setDisable(true);

        deactivate.setVisible(true);
        deactivate.setDisable(true);

        List<Control> controls = new ArrayList<>() {{add(turnsField); add(deactivate);}};

        if (response.getJSONObject("riddle").has("message")) {
            terminalMessage = response.getJSONObject("riddle").get("message").toString().replace("<team>", User.getInstance().getTeam());
        } else if (response.has("msg")){
            terminalMessage = ">>" + response.get("msg").toString();
        } else {
            System.err.println("There was a problem retrieving riddle " + RIDDLEID + " from the Backend.");
        }
        timeline = UIUtility.animateTypewriterConstant(terminal, terminalMessage, controls);
    };

    @FXML
    public void skipTypewriterAnimation(){
        // Stop the timeline when the stop button is clicked
        if (timeline != null) {
            timeline.stop();
            // print text
            terminal.setText(terminalMessage);
            // Enable controls after stopping animation
            List<Control> controlsToEnable = List.of(turnsField, deactivate); // Add more controls if needed
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }
}


