package at.ac.fhcampuswien.trackingdevice.utils;

import org.json.JSONObject;

public interface ApiResponseHandler {
    void onResolve(JSONObject response);
}
