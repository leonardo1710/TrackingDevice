package at.ac.fhcampuswien.trackingdevice.configs;

public class Config {
    private static Config instance = null;

    private final String dbFolderLink = "https://www.dropbox.com/sh/iikdtfgy069ptj3/AACZTLhZ_FQQdkCpu68q9Wota?dl=0";

    private Config(){}

    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    public String getEnvelopeLink(){
        return this.dbFolderLink;
    }
}
