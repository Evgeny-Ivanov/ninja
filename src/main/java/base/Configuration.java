package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by stalker on 01.11.15.
 */
public final class Configuration {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    private String signinPageUrl;
    private String signupPageUrl;
    private String logoutPageUrl;
    private String mainPageUrl;
    private String adminPageUrl;
    private String resourcesDirectory;

    private String gameSocketUrl;
    private String gameSocketHost;
    private String gameSocketPort;


    private String host;
    private String port;

    public Configuration(@NotNull String propertiesFile) {

        try (final FileInputStream fis = new FileInputStream(propertiesFile)) {
            final Properties properties = new Properties();
            properties.load(fis);

            host = properties.getProperty("host");
            port = properties.getProperty("port");

            gameSocketHost = properties.getProperty("gameSocketHost");
            gameSocketPort = properties.getProperty("gameSocketPort");

            signinPageUrl = properties.getProperty("signinPageUrl");
            signupPageUrl = properties.getProperty("signupPageUrl");
            logoutPageUrl = properties.getProperty("logoutPageUrl");
            mainPageUrl = properties.getProperty("mainPageUrl");
            adminPageUrl = properties.getProperty("adminPageUrl");
            gameSocketUrl = properties.getProperty("gameSocketUrl");
            resourcesDirectory = properties.getProperty("resourcesDirectory");

        } catch (IOException e) {
            LOGGER.error(e);
            return;
        }

        if (port == null || host == null) {
            LOGGER.error("Port or host is null");
            throw new NullPointerException("Port or host is null");
        }

        if (gameSocketPort == null || gameSocketHost == null) {
            LOGGER.error("Port or host is null");
            throw new NullPointerException("Port or host is null");
        }

        if(signinPageUrl == null || signupPageUrl == null){
            LOGGER.error("Servlet url is null");
            throw new NullPointerException("Servlet Url is null");
        }
        if(logoutPageUrl == null || mainPageUrl == null){
            LOGGER.error("Servlet url is null");
            throw new NullPointerException("Servlet Url is null");
        }

        if(adminPageUrl == null || gameSocketUrl == null){
            LOGGER.error("Servlet url is null");
            throw new NullPointerException("Servlet Url is null");
        }

        LOGGER.info(this);
    }

    public String getGameSocketHost() {
        return gameSocketHost;
    }

    public String getGameSocketPort() {
        return gameSocketPort;
    }

    public int getPort(){
        return new Integer(port);
    }
    @Nullable
    public String getHost(){
        return host;
    }

    @Nullable
    public String getSigninPageUrl(){
        return signinPageUrl;
    }
    @Nullable
    public String getSignupPageUrl(){
        return signupPageUrl;
    }
    @Nullable
    public String getMainPageUrl(){ return mainPageUrl; }
    @Nullable
    public String getLogoutPageUrl(){
        return logoutPageUrl;
    }
    @Nullable
    public String getAdminPageUrl(){
        return adminPageUrl;
    }
    @NotNull
    public String getGameSocketUrl(){
        if(gameSocketUrl != null) return  gameSocketUrl;
        return "/gameplay";
    }

    @NotNull
    public String getResourcesDirectory() {
        if(resourcesDirectory != null) return  resourcesDirectory;
        return "./data";
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "signinPageUrl='" + signinPageUrl + '\'' +
                ", signupPageUrl='" + signupPageUrl + '\'' +
                ", logoutPageUrl='" + logoutPageUrl + '\'' +
                ", mainPageUrl='" + mainPageUrl + '\'' +
                ", adminPageUrl='" + adminPageUrl + '\'' +
                ", gameSocketUrl='" + gameSocketUrl + '\'' +
                ", gameSocketHost='" + gameSocketHost + '\'' +
                ", gameSocketPort='" + gameSocketPort + '\'' +
                ", resourcesDirectory='" + resourcesDirectory + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
