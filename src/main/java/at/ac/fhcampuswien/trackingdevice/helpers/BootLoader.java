package at.ac.fhcampuswien.trackingdevice.helpers;

import at.ac.fhcampuswien.trackingdevice.utils.ApiResponseHandler;

public class BootLoader {
    private final TrackingDevice trackingDevice;

    public BootLoader(TrackingDevice trackingDevice) {
        this.trackingDevice = trackingDevice;
    }

    public void load(ApiResponseHandler apiResponseHandler) throws TrackingDeviceBootException {
        System.out.println("Try booting tracking device...");
        System.out.println("Try booting tracking device...");
        System.out.println("Try booting tracking device...");
        if(trackingDevice != null) {
            trackingDevice.boot(apiResponseHandler);
        } else {
            throw new TrackingDeviceBootException("Tracking device is not available.");
        }
    }

}
