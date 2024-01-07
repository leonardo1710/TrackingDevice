package at.ac.fhcampuswien.trackingdevice.configs;

import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class User implements Serializable{
    private String team;
    private String token;
    private static User instance = null;
    private String message;
    private int currentRiddle;

    public String getTeam() {
        return team;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public int getCurrentRiddle() {
        return currentRiddle;
    }

    public void setCurrentRiddle(int currentRiddle) {
        if(this.currentRiddle < currentRiddle) {
            this.currentRiddle = currentRiddle;
            serializeUser(this);
        }
    }

    private static final String USERFILE = "build/resources/main/files/user.ser";

    public boolean login(String team, String password) {
        Map<String, String> reqArguments = Map.of("name", team, "password", password);
        JSONObject response = null;
        try {
            response = API.buildPostRequest(reqArguments, "/api/team/login", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null && response.get("msg").equals("Login successful")) {
            this.team = team;
            this.token = response.get("access_token").toString();
            this.currentRiddle = Integer.parseInt(response.get("currentRiddle").toString());
            serializeUser(this);
            return true;
        } else {
            return false;
        }
    }
    public boolean register(String team, String password, String member1, String member2, String member3) {
        String members = String.join(", ", new HashSet<>(Arrays.asList(member1, member2, member3)));
        // send POST request to register
        Map<String, String> reqArguments = Map.of("name", team, "password", password, "members", members);
        String path = "/api/team";
        JSONObject response = null;
        try {
            response = API.buildPostRequest(reqArguments, path, false);

            if (response.has("access_token")) {
                token = response.get("access_token").toString();
                this.team = team;
                serializeUser(this);
                return true;
            } else if (response.has("message")) {
                message = response.get("message").toString();
                return false;
            } else if (response.has("err")) {
                message = response.get("err").toString();
                System.out.println("> " + message);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // unpredicted behaviour
        return false;
    }

    public static User getInstance() {
        if (instance == null) {
            // try to load existing object:
            User tmp = deserializeUser();
            instance = tmp != null ? tmp : new User();
        }
        return instance;
    }

    private static void serializeUser(User user) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(USERFILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static User deserializeUser() {
        User user = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(USERFILE);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            user = (User) o;
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("> No user object created yet.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isRegistered() {
        return token != null;
    }


}
