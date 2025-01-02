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
    private JFXToggleButton blueToggle;

    @FXML
    private JFXToggleButton yellowToggle;

    @FXML
    private JFXToggleButton redToggle;

    @FXML
    private JFXToggleButton greenToggle;

    @FXML
    private JFXToggleButton violetToggle;

    @FXML
    private Button resetBtn;

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
        yellowToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> yellow switch activated");
            yellowToggle.setDisable(true);
            activateToggle("#FFFF00");
        });

        redToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> red switch activated");
            redToggle.setDisable(true);
            activateToggle("#FF0000");
        });

        blueToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> blue switch activated.");
            blueToggle.setDisable(true);
            activateToggle("#0000FF");
        });

        greenToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> green switch activated");
            greenToggle.setDisable(true);
            activateToggle("#00FF00");
        });

        violetToggle.setOnAction((ActionEvent e) -> {
            System.out.println("> violet switch activated.");
            violetToggle.setDisable(true);
            activateToggle("#FF00FF");
        });
    }

    @FXML
    private void resetToggles() {
        greenToggle.setSelected(false);
        greenToggle.setDisable(false);
        blueToggle.setSelected(false);
        blueToggle.setDisable(false);
        yellowToggle.setDisable(false);
        yellowToggle.setSelected(false);
        violetToggle.setDisable(false);
        violetToggle.setSelected(false);
        redToggle.setDisable(false);
        redToggle.setSelected(false);
        message = "";
        togglesActivated = 0;
        System.out.println("> reset of switches successful.");
    }

    private void activateToggle(String toggleActivated){
        message += toggleActivated;
        System.out.println(message);
        togglesActivated++;
        if(togglesActivated == 4){
            sendActivation();
        }
    }

    private void sendActivation(){
        riddleSceneBaseController.submit(message, (response -> { resetToggles(); }));
    }

    // DO NOT CHANGE THIS VARIABLE
    private final ApiResponseHandler onResolveApiResponse = (response) -> {
        List<Control> controls = new ArrayList<>() {{add(redToggle);add(resetBtn);add(yellowToggle);add(blueToggle);add(greenToggle);add(violetToggle);}};
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
        List<Control> controls = new ArrayList<>() {{add(redToggle);add(resetBtn);add(yellowToggle);add(blueToggle);add(greenToggle);add(violetToggle);}};

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
