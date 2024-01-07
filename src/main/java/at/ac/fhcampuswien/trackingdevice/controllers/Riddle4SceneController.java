package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class Riddle4SceneController extends BaseController implements Initializable {
    private final String RIDDLEID = "4";

    @FXML
    private TextField datetime;

    @FXML
    private Button submit;

    @FXML
    private Text terminal;

    @FXML
    private ImageView image;

    @FXML
    private ScrollPane scrollPane;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    private Timeline timeline;

    static public String terminalMessage = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // DO NOT CHANGE THIS METHOD CALL
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);

        // TODO: test your implementation here
        calculateDateTime(0);
    }

    // TODO: Calculate the correct date and time at which the portal will open
    private void calculateDateTime(long timestampInMilliseconds) {
        String datetime = "";
        System.out.println(">> calculateDateTime >> the calculated date and time is: " + datetime);
    }

    // DO NOT CHANGE THIS METHOD
    public void submitDatetime() {
        riddleSceneBaseController.submit(datetime.getText(), null);
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> image.setImage(background));

        submit.setVisible(true);
        submit.setDisable(true);

        datetime.setVisible(true);
        datetime.setDisable(true);

        List<Control> controls = new ArrayList<>() {{add(submit); add(datetime);}};

        if (response.getJSONObject("riddle").has("message")) {
            terminalMessage = response.getJSONObject("riddle").get("message").toString().replace("<team>", User.getInstance().getTeam());
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
            List<Control> controlsToEnable = List.of(submit, datetime); // Add more controls if needed
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }

}
