package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.helpers.Localizer;
import at.ac.fhcampuswien.trackingdevice.helpers.PortalTracker;
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
    static public boolean portalTrackerActive = false;

    @FXML
    private Button localizeButton;

    @FXML
    private Text terminal;

    @FXML
    private ImageView image;

    @FXML
    private ScrollPane scrollPane;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    public String noteFromDocCrusty;

    private Timeline timeline;

    static public String terminalMessage = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // DO NOT CHANGE THIS METHOD CALL
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);
    }

    public static String getField(int x, int y){
        // TODO: implement the algorithm
        return "(X/Y)"; // use the provided format
    }

    public void onLocalizeButtonClicked() {
        //TODO: implement getField and use its return with portalTracker
        String field = getField(75, 53);
        //TODO: use portalTracker to find Portal (have a look at the PortalTracker class to find out how)
        Localizer portalTracker = new PortalTracker();

        // DO NOT CHANGE ANYTHING BELOW THIS LINE
        if (!portalTrackerActive) {
            terminalMessage = noteFromDocCrusty;
            System.err.println("Portaltracker: Device incomplete! Localizer missing.");
        }
        timeline = UIUtility.animateTypewriterConstant(terminal, terminalMessage, new ArrayList<>() {{add(localizeButton);}});
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img").toString());
        Platform.runLater(() -> image.setImage(background));

        localizeButton.setDisable(true);

        List<Control> controls = new ArrayList<>() {{add(localizeButton);}};

        noteFromDocCrusty = "Hinterlegte Notiz von Dr. Victor Crustacis: Dieses Control funktioniert noch nicht wie es sollte.  Ihr müsst das korrekte Feld im Format (x/y) ermitteln und an die zugehörige Methode im PortalTracker übergeben, um eure Eingabe zu verifizieren.\n" +
                "Vergesst dabei nicht, das Localizer-Interface im PortalTracker zu implementieren – ohne diese Anpassung wird das System das Portal nicht ordnungsgemäß verfolgen können. Viel Erfolg!";

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

    public void printProfHint() {
        System.out.println(">> Hinterlegte Notiz von Prof. Dr. Victor Crustacis: Dieses Kuvert ist nur ein Abbild des Originals. Das Original holt bitte von meinen Kollegen!");
    }

    @FXML
    public void skipTypewriterAnimation(){
        // Stop the timeline when the stop button is clicked
        if (timeline != null) {
            timeline.stop();
            // print text
            terminal.setText(terminalMessage);
            // Enable controls after stopping animation
            List<Control> controlsToEnable = List.of(localizeButton); // Add more controls if needed
            controlsToEnable.forEach(control -> control.setDisable(false));
        }
    }

}
