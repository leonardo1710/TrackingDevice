package at.ac.fhcampuswien.trackingdevice.utils;

import at.ac.fhcampuswien.trackingdevice.configs.API;
import at.ac.fhcampuswien.trackingdevice.configs.User;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Map;

public class RiddleSceneBaseController {

    private final String RIDDLEID;

    public RiddleSceneBaseController(String RIDDLEID) {
        this.RIDDLEID = RIDDLEID;
    }

    public void initializeRiddleScene(ApiResponseHandler apiResponseHandler) {
        // separate non-FX thread
        new Thread(() -> {
            JSONObject response = null;
            try {
                System.out.println("> Sending GET request riddle " + RIDDLEID + " ...");
                response = API.createGetRequest("/api/team-riddle/getriddle/" + RIDDLEID);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(response != null && response.has("msg")) {
                System.out.println("> GET riddle " + RIDDLEID + " response: " + response.get("msg"));
                if(response.get("msg").equals("Access granted")) {
                    apiResponseHandler.onResolve(response);
                }
            } else {
                response.append("msg", "There was a problem retrieving riddle " + RIDDLEID + " from the Backend.");
                apiResponseHandler.onResolve(response);
            }
        }).start();
    }

    public void submit(String message, ApiResponseHandler apiResponseHandler) {
        Map<String, String> reqArguments = Map.of("code", message);
        JSONObject response = null;
        try {
            System.out.println("> Sending POST request riddle " + RIDDLEID + " solved.");
            response = API.buildPostRequest(reqArguments, "/api/team-riddle/riddlesolved/" + RIDDLEID, true);
            System.out.println("> Response to POST request riddle " + RIDDLEID + " solved: " + response.get("msg"));
        } catch (IOException e) {
            System.err.println("IOException riddle " + RIDDLEID + "  POST");
        } catch (Exception e) {
            System.err.println("Exception riddle " + RIDDLEID + " POST");
        }

        if (response != null && response.get("msg").equals("Riddle solved")) {
            User.getInstance().setCurrentRiddle(User.getInstance().getCurrentRiddle() + 1);
            Platform.runLater(() -> {
                try {
                    SceneLoader.getInstance(null, "riddle" + User.getInstance().getCurrentRiddle() + "Scene.fxml", "Riddle " + User.getInstance().getCurrentRiddle()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (response != null && response.has("msg")) {
            System.err.println(response.get("msg") + " - try again!");
            if (apiResponseHandler != null)
                apiResponseHandler.onResolve(response);
        } else {
            System.err.println("There was a problem submitting riddle " + RIDDLEID + " to the Backend.");
        }
    }

    public static void initializeZoomableImageView(ImageView image, ScrollPane scrollPane) {
        DoubleProperty zoomProperty = new SimpleDoubleProperty(600);

        image.setFitHeight(zoomProperty.get());
        image.setFitWidth(zoomProperty.get());

        zoomProperty.addListener((observable, oldValue, newValue) -> {
            image.setFitWidth(newValue.doubleValue());
            image.setFitHeight(newValue.doubleValue());
        });

        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) { // check for Ctrl key (command on macOS is treated as Ctrl)
                event.consume(); // consume the event so it doesn't propagate
                double delta = event.getDeltaY();

                if (delta > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1); // zoom in
                } else if (delta < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1); // zoom out
                }

                // boundaries for zoom level
                double minZoom = 100;
                double maxZoom = 2000;
                zoomProperty.set(Math.max(minZoom, Math.min(maxZoom, zoomProperty.get())));
            }
        });

        // preserve the aspect ratio of the image
        image.preserveRatioProperty().set(true);
    }
}
