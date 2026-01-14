package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.animation.Timeline;

import java.net.URL;
import java.util.*;

public class Riddle3SceneController extends BaseController implements Initializable {
    private final String RIDDLEID = "3";
    @FXML
    private Button verify;

    @FXML
    private TextField messageField;

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
    }

    private String cipher(String message, int offset) {
        if (offset <= 0) {
            System.err.println("No valid offset defined.");
            return message;
        }

        // TODO write cipher algorithm and return encoded message
        // consider only characters from A-Z and a-z

        return "";
    }

    @FXML
    public void verifyAlgorithm(){
        if(messageField.getText().equals("")){
            UIUtility.animateTypewriterConstant(terminal, ">>Please enter the password.", null);
            return;
        }

        // TODO: implement complete the cipher method above
        // TODO: provide correct offset
        int offset = 0;
        String code = cipher(messageField.getText(), offset);

        riddleSceneBaseController.submit(code, null);
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> image.setImage(background));

        verify.setVisible(true);
        verify.setDisable(true);

        if (response.getJSONObject("riddle").has("message")) {
            terminalMessage = response.getJSONObject("riddle").get("message").toString().replace("<team>", User.getInstance().getTeam());
        } else if (response.has("msg")){
            terminalMessage = ">>" + response.get("msg").toString();
        } else {
            System.err.println("There was a problem retrieving riddle " + RIDDLEID + " from the Backend.");
        }

        timeline = UIUtility.animateTypewriterConstant(terminal, terminalMessage, new ArrayList<>() {{add(verify);}});
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
            List<Control> controlsToEnable = List.of(verify); // Add more controls if needed
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }
}
