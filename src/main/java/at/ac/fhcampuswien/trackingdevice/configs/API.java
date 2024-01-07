package at.ac.fhcampuswien.trackingdevice.configs;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import java.net.http.HttpClient;

public class API {
    public static final String BASE_URI = "http://77.237.53.210";

    public static JSONObject buildGetRequest(String path) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        var uri = URI.create(BASE_URI + path);

        var request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .header("Authorization", "Bearer " + User.getInstance().getToken())
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        return new JSONObject(new JSONTokener(response.body()));
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject createGetRequest(String path) throws IOException {
        URL url = new URL(BASE_URI + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + User.getInstance().getToken());
        conn.connect();
        // handle response
        System.out.println("> Connection response message: " + conn.getResponseMessage());
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String jsonText = readAll(in);
        return new JSONObject(jsonText);
    }

    public static JSONObject buildPostRequest(Map<String, String> params, String path, boolean authorizationNeeded) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        var uri = URI.create(BASE_URI + path);

        var request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject(params).toString()))
                .header("Content-Type", "application/json");

        if(authorizationNeeded) {
            request.header("Authorization", "Bearer " + User.getInstance().getToken());
        }

        var response = client.send(request.build(), HttpResponse.BodyHandlers.ofInputStream());

        // check if request was successful in range 200-299
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return new JSONObject(new JSONTokener(response.body()));
        } else if (response.statusCode() == 409) {
            return new JSONObject("{\"err\": \"Error: team name already in use.\"}, \"status\": " + response.statusCode() + "}");
        } else {
            // return JSON with error message and status code
            return new JSONObject("{\"err\": \"Error: cannot send post request to " + uri + "\"}, \"status\": " + response.statusCode() + "}");
        }
    }

    // make post request:
    // https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily/4206094#4206094
    public static JSONObject createPostRequest(Map<String,String> params, String path, boolean authorizationNeeded) throws IOException {
        URL url = new URL(BASE_URI + path);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        if(authorizationNeeded){
            conn.setRequestProperty("Authorization", "Bearer " + User.getInstance().getToken());
        }
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        // handle response
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String jsonText = readAll(in);
        return new JSONObject(jsonText);
    }

    static {
        disableSslVerification();
    }

    // This is a workaround due to SSL Problems with the Server
    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
