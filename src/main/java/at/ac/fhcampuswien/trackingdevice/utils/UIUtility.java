package at.ac.fhcampuswien.trackingdevice.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.List;

public class UIUtility {
    public static Timeline animateTypewriterConstant(Text text, String str, List<Control> controls){
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(50),
                event -> {
                    if (i.get() > str.length()) {
                        timeline.stop();
                        if(controls != null){
                            for (Control c : controls) {
                                c.setDisable(false);
                            }
                        }
                    } else {
                        text.setText(str.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return timeline;
    }
}
