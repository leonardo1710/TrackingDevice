package at.ac.fhcampuswien.trackingdevice.controllers;

import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController extends BaseController {
    private final int MAX_LENGTH = 30;
    @FXML
    private Label message;

    @FXML
    private TextField team;

    @FXML
    private TextField password;

    @FXML
    private TextField password2;

    @FXML
    private TextField member1;

    @FXML
    private TextField member2;

    @FXML
    private TextField member3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limitTeamNameLength();
    }

    private void limitTeamNameLength() {
        team.textProperty().addListener((observable, oldValue, newValue) -> {
            String copy = oldValue;
            if (newValue.length() > MAX_LENGTH) {
                copy = copy.substring(0, MAX_LENGTH);
                team.setText(copy);
            }
        });
    }

    private boolean validateRequiredFields() {
        if(team.getText().length() == 0){
            message.setText("You have to provide a name for your team.");
            System.out.println("> No team name provided.");
            System.out.println("> Trying to register again.");
            return false;
        }
        if(password.getText().length() == 0){
            message.setText("You have to provide a password for your team.");
            System.out.println("> No password provided.");
            System.out.println("> Trying to register again.");
            return false;
        }
        return true;
    }
    @FXML
    protected void login(ActionEvent event) {
        System.out.println("> Trying to login...");

        if(validateRequiredFields()) {
            boolean success = User.getInstance().login(team.getText(), password.getText());
            if(success) {
                try {
                    SceneLoader.getInstance(stage, "riddle"+ (User.getInstance().getCurrentRiddle())+"Scene.fxml", "Riddle " + (User.getInstance().getCurrentRiddle() + 1) ).start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("> " + User.getInstance().getMessage());
                System.out.println("> Trying to register again.");
                message.setText(User.getInstance().getMessage());
            }
        }
    }

    @FXML
    protected void register(ActionEvent action) {
        System.out.println("> Trying to register...");
        if(validateRequiredFields()) {
            if(member1.getText().length() == 0 || member2.getText().length() == 0){
                message.setText("Name at least two team members.");
                System.out.println("> One or two member names not set.");
                System.out.println("> Trying to register again.");
                return;
            }
            boolean success = User.getInstance().register(team.getText(), password.getText(), member1.getText(), member2.getText(), member3.getText());

            if(success) {
                try {
                    User.getInstance().setCurrentRiddle(2);
                    SceneLoader.getInstance(stage, "riddle"+User.getInstance().getCurrentRiddle()+"Scene.fxml", "Riddle " + User.getInstance().getCurrentRiddle()).start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("> " + User.getInstance().getMessage());
                System.out.println("> Trying to register again.");
                message.setText(User.getInstance().getMessage());
            }
        }

    }
}
