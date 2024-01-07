package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.RiddleSceneBaseController;
import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;
import at.ac.fhcampuswien.trackingdevice.utils.UIUtility;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Riddle7SceneController extends BaseController {

    private static final String RIDDLEID = "7";
    private String message = "";
    private int togglesActivated = 0;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane canvas;

    @FXML
    private Text terminal;

    @FXML
    private ImageView image;

    @FXML
    private JFXToggleButton parsnipsToggle;

    @FXML
    private JFXToggleButton flatteryToggle;

    @FXML
    private JFXToggleButton noToggle;

    @FXML
    private JFXToggleButton butterToggle;

    @FXML
    private JFXToggleButton dothToggle;

    @FXML
    private Button resetBtn;

    @FXML
    private Button openEnvelopeBtn;

    private final RiddleSceneBaseController riddleSceneBaseController = new RiddleSceneBaseController(RIDDLEID);

    private Timeline timeline;

    static public String terminalMessage = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        riddleSceneBaseController.initializeRiddleScene(onResolveApiResponse);
    }

    private void positionAndScaleCreatures(ImageView creature1, ImageView creature2, ImageView creature3){
        creature1.setScaleX(0.7);
        creature1.setScaleY(0.7);
        creature1.setTranslateX(120);
        creature2.setScaleX(0.7);
        creature2.setScaleY(0.7);
        creature2.setTranslateX(720);
        creature2.setTranslateY(-70);
        creature3.setScaleX(0.7);
        creature3.setScaleY(0.7);
        creature3.setTranslateX(-520);
    }

    private void translateAnimationForCreature(ImageView image, int toX, int toY){
        TranslateTransition transition = new TranslateTransition(Duration.millis(1500), image);
        transition.setFromX(image.getTranslateX());
        transition.setFromY(image.getTranslateY());
        transition.setToX(image.getTranslateX() - toX);
        transition.setToY(image.getTranslateY() - toY);
        transition.playFromStart();
    }

    private void initializeSwitches(){
        butterToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> butter switch activated");
            butterToggle.setDisable(true);
            activateToggle("Butter");
        });

        dothToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> doth switch activated");
            dothToggle.setDisable(true);
            activateToggle("Doth");
        });

        parsnipsToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> parsnips switch activated.");
            parsnipsToggle.setDisable(true);
            activateToggle("Parsnips");
        });

        noToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> no switch activated");
            noToggle.setDisable(true);
            activateToggle("No");
        });

        flatteryToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> flattery switch activated.");
            flatteryToggle.setDisable(true);
            activateToggle("Flattery");
        });
    }

    @FXML
    private void openEnvelope(){
        try {
            Desktop.getDesktop().browse(new URL("https://www.dropbox.com/sh/qwszyjcbel3ab7d/AACTX9787nk4Q5epM_GpqQd-a?dl=0").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetToggles() {
        parsnipsToggle.setSelected(false);
        flatteryToggle.setSelected(false);
        dothToggle.setDisable(false);
        parsnipsToggle.setDisable(false);
        flatteryToggle.setDisable(false);
        dothToggle.setSelected(false);
        butterToggle.setSelected(false);
        noToggle.setSelected(false);
        butterToggle.setDisable(false);
        noToggle.setDisable(false);
        message = "";
        togglesActivated = 0;
        System.out.println("> reset of switches successful.");
    }

    private void activateToggle(String toggleActivated){
        message += toggleActivated;
        togglesActivated++;
        if(togglesActivated == 5){
            sendActivation();
        }
    }

    private void sendActivation(){
        riddleSceneBaseController.submit(message, (response -> { resetToggles(); }));
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        List<Control> controls = new ArrayList<>() {{add(openEnvelopeBtn);add(butterToggle);add(resetBtn);add(noToggle);add(parsnipsToggle);add(flatteryToggle);add(dothToggle);}};
        final Image background = new Image(API.BASE_URI + response.getJSONObject("riddle").get("img1").toString());
        // update image on FX thread
        final ImageView creature1 = new ImageView(new Image(API.BASE_URI + response.getJSONObject("riddle").get("img2").toString()));
        final ImageView creature2 = new ImageView(new Image(API.BASE_URI + response.getJSONObject("riddle").get("img3").toString()));
        final ImageView creature3 = new ImageView(new Image(API.BASE_URI + response.getJSONObject("riddle").get("img4").toString()));
        Platform.runLater(() -> {
            this.image.setImage(background);
            canvas.getChildren().addAll(creature2, creature3, creature1);
            positionAndScaleCreatures(creature1, creature2, creature3);
            translateAnimationForCreature(creature1, 0, 100);
            translateAnimationForCreature(creature2, 320, 0);
            translateAnimationForCreature(creature3, -420, 0);
        });

        scrollPane.addEventFilter(ScrollEvent.SCROLL, Event::consume);
        initializeSwitches();
        for (Control c : controls) {
            c.setVisible(true);
            c.setDisable(true);
        }


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
        List<Control> controls = new ArrayList<>() {{add(openEnvelopeBtn);add(butterToggle);add(resetBtn);add(noToggle);add(parsnipsToggle);add(flatteryToggle);add(dothToggle);}};

        // Stop the timeline when the stop button is clicked
        if (timeline != null) {
            timeline.stop();
            // print text
            terminal.setText(terminalMessage);
            // Enable controls after stopping animation
            controls.forEach(control -> control.setDisable(false));
        }
    }
}
