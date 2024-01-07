module at.ac.fhcampuswien.trackingdevice {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.json;
    requires com.jfoenix;
    requires java.desktop;
    requires java.net.http;
    requires javafx.media;

    opens at.ac.fhcampuswien.trackingdevice to javafx.fxml;
    opens at.ac.fhcampuswien.trackingdevice.controllers to javafx.fxml;
    exports at.ac.fhcampuswien.trackingdevice;

}