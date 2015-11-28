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
    @NotNull
    @SuppressWarnings("ConstantConditions")
    static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    @NotNull
    private String signinPageUrl;
    @NotNull
    private String signupPageUrl;
    @NotNull
    private String logoutPageUrl;
    @NotNull
    private String mainPageUrl;
    @NotNull
    private String adminPageUrl;
    @NotNull
    private String resourcesDirectory;
    @NotNull
    private String gameSocketUrl;
    @NotNull
    private String gameSocketHost;
    @NotNull
    private String gameSocketPort;
    @NotNull
    private String host;
    @NotNull
    private String port;

    public Configuration(@NotNull String propertiesFile) {
        @NotNull
        final Properties properties = new Properties();

        try (final FileInputStream fis = new FileInputStream(propertiesFile)) {
            properties.load(fis);
        } catch (IOException ignored) {
            LOGGER.error("IOException");
            throw new RuntimeException();
        }

        String readedHost = properties.getProperty("host");
        if (readedHost == null) {
            LOGGER.error("host is null");
            throw new NullPointerException();
        }
        host = readedHost;

        String readedPort = properties.getProperty("port");
        if (readedPort == null) {
            LOGGER.error("port is null");
            throw new NullPointerException();
        }
        port = readedPort;

        String readedGameSocketHost = properties.getProperty("gameSocketHost");
        if (readedGameSocketHost == null) {
            LOGGER.error("gameSocketHost is null");
            throw new NullPointerException();
        }
        gameSocketHost = readedHost;

        String readedGameSocketPort = properties.getProperty("gameSocketPort");
        if (readedGameSocketPort == null) {
            LOGGER.error("gameSocketPort is null");
            throw new NullPointerException();
        }
        gameSocketPort = readedGameSocketPort;

        String readedSigninPageUrl = properties.getProperty("signinPageUrl");
        if (readedSigninPageUrl == null) {
            LOGGER.error("signinPageUrl is null");
            throw new NullPointerException();
        }
        signinPageUrl = readedSigninPageUrl;

        String readedSignupPageUrl = properties.getProperty("signupPageUrl");
        if (readedSignupPageUrl == null) {
            LOGGER.error("signupPageUrl is null");
            throw new NullPointerException();
        }
        signupPageUrl = readedSignupPageUrl;

        String readedLogoutPageUrl = properties.getProperty("logoutPageUrl");
        if (readedLogoutPageUrl == null) {
            LOGGER.error("logoutPageUrl is null");
            throw new NullPointerException();
        }
        logoutPageUrl = readedLogoutPageUrl;

        String readedMainPageUrl = properties.getProperty("mainPageUrl");
        if (readedMainPageUrl == null) {
            LOGGER.error("mainPageUrl is null");
            throw new NullPointerException();
        }
        mainPageUrl = readedMainPageUrl;

        String readedAdminPageUrl = properties.getProperty("adminPageUrl");
        if (readedAdminPageUrl == null) {
            LOGGER.error("adminPageUrl is null");
            throw new NullPointerException();
        }
        adminPageUrl = readedAdminPageUrl;

        String readedGameSocketUrl = properties.getProperty("gameSocketUrl");
        if (readedGameSocketUrl == null) {
            LOGGER.error("gameSocketUrl is null");
            throw new NullPointerException();
        }
        gameSocketUrl = readedGameSocketUrl;

        String readedResourcesDirectory = properties.getProperty("resourcesDirectory");
        if (readedResourcesDirectory == null) {
            LOGGER.error("resourcesDirectory is null");
            throw new NullPointerException();
        }
        resourcesDirectory = readedResourcesDirectory;

        LOGGER.info(this);
    }

    @NotNull
    public String getGameSocketHost() {
        return gameSocketHost;
    }

    @NotNull
    public String getGameSocketPort() {
        return gameSocketPort;
    }

    public int getPort() {
        return new Integer(port);
    }

    @NotNull
    public String getHost() {
        return host;
    }

    @NotNull
    public String getSigninPageUrl() {
        return signinPageUrl;
    }

    @NotNull
    public String getSignupPageUrl() {
        return signupPageUrl;
    }

    @NotNull
    public String getMainPageUrl() {
        return mainPageUrl;
    }

    @NotNull
    public String getLogoutPageUrl() {
        return logoutPageUrl;
    }

    @NotNull
    public String getAdminPageUrl() {
        return adminPageUrl;
    }

    @NotNull
    public String getGameSocketUrl() {
        return gameSocketUrl;
    }

    @NotNull
    public String getResourcesDirectory() {
        return resourcesDirectory;
    }

    @Override
    public String toString() {
        return "Configuration{" +'\n' +
                "signinPageUrl='" + signinPageUrl + '\'' + '\n' +
                ", signupPageUrl='" + signupPageUrl + '\'' +'\n' +
                ", logoutPageUrl='" + logoutPageUrl + '\'' +'\n' +
                ", mainPageUrl='" + mainPageUrl + '\'' +'\n' +
                ", adminPageUrl='" + adminPageUrl + '\'' +'\n' +
                ", resourcesDirectory='" + resourcesDirectory + '\'' +'\n' +
                ", gameSocketUrl='" + gameSocketUrl + '\'' +'\n' +
                ", gameSocketHost='" + gameSocketHost + '\'' +'\n' +
                ", gameSocketPort='" + gameSocketPort + '\'' +'\n' +
                ", host='" + host + '\'' +'\n' +
                ", port='" + port + '\'' +'\n' +
                '}';
    }
}
