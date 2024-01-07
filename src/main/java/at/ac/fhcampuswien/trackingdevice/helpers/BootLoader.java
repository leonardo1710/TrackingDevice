package at.ac.fhcampuswien.trackingdevice.helpers;

public class BootLoader {
    private final TrackingDevice trackingDevice;

    public BootLoader(TrackingDevice trackingDevice) {
        this.trackingDevice = trackingDevice;
    }

    public void load() throws TrackingDeviceBootException {
        System.out.println("Try booting tracking device...");
        System.out.println("Try booting tracking device...");
        System.out.println("Try booting tracking device...");
        if(trackingDevice != null) {
            trackingDevice.boot();
        } else {
            throw new TrackingDeviceBootException("Tracking device is not available.");
        }
    }

}
