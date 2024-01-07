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
    static public boolean portalTrackerActive = false;
    static public String terminalMessage = "";

    @FXML
    private ScrollPane scrollPane;


    @FXML
    private ImageView image;

    @FXML
    public TextField coordinates;

    @FXML
    public Button localizeButton;

    @FXML
    public Text terminal;

    public String noteFromDocCrusty;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);
    }

    public void onLocalizeButtonClicked() {
        //TODO: use this with portalTracker Object (DON'T CHANGE THIS LINE)
        String coordinatesValue = coordinates.getText();
        //TODO: use portalTracker to find Portal (have a look at the PortalTracker class to find out how)
        Localizer portalTracker = new PortalTracker();

        // DO NOT CHANGE ANYTHING BELOW THIS LINE
        if (!portalTrackerActive) {
            terminalMessage = noteFromDocCrusty;
            System.err.println("Portaltracker: Device incomplete! Localizer missing.");
        }
        timeline = UIUtility.animateTypewriterConstant(terminal, terminalMessage, new ArrayList<>() {{add(localizeButton);}});
    }

    public void printProfHint() {
        System.out.println(">> Hinterlegte Notiz von Prof. Dr. Victor Crustacis: Dieses Kuvert ist nur ein Abbild des Originals. Das Original holt bitte von meinen Kollegen!");
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> image.setImage(background));

        coordinates.setVisible(true);
        coordinates.setDisable(true);

        localizeButton.setVisible(true);
        localizeButton.setDisable(true);

        List<Control> controls = new ArrayList<>() {{add(coordinates); add(localizeButton);}};

        noteFromDocCrusty = "Hinterlegte Notiz von Dr. Victor Crustacis: Dieses Control funktioniert noch nicht wie es sollte. Das Problem ist die Klasse Riddle5SceneController und hier im speziellen die Methode 'onLocalizeButtonClicked'. Ich habe es nicht geschafft den Code zu vervollständigen, damit das Portal getrackt werden kann.";

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
            List<Control> controlsToEnable = List.of(coordinates, localizeButton); // Add more controls if needed
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }
}
