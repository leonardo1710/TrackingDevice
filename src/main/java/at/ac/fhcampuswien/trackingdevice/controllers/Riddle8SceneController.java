package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.json.JSONObject;

public class Riddle8SceneController extends BaseController {
    private static final String RIDDLEID = "8";

    @FXML
    private Text terminal;

    @FXML
    private ImageView image;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> image.setImage(background));
        UIUtility.animateTypewriterConstant(terminal, response.getJSONObject("riddle").get("message").toString().replace("<team>", User.getInstance().getTeam()), null);


        if (response.getJSONObject("riddle").has("message")) {
            UIUtility.animateTypewriterConstant(terminal, response.getJSONObject("riddle").get("message").toString().replace("<team>", User.getInstance().getTeam()), null);
        } else if (response.has("msg")){
            UIUtility.animateTypewriterConstant(terminal, ">>" + response.get("msg").toString(), null);
        } else {
            System.err.println("There was a problem retrieving riddle " + RIDDLEID + " from the Backend.");
        }
    };
}
