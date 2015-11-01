package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by stalker on 01.11.15.
 */
public class Configuration {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    private static Configuration configuration;
    private static final String PROPERTIES_FILE = "cfg/server.properties";

    private String signinPageUrl;
    private String signupPageUrl;
    private String logoutPageUrl;
    private String mainPageUrl;
    private String adminPageUrl;
    private String gameSocketUrl;

    private String port;
    private String host;

    private Configuration(){

        try (final FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            final Properties properties = new Properties();
            properties.load(fis);

            host = properties.getProperty("host");
            port = properties.getProperty("port");

            signinPageUrl = properties.getProperty("signinPageUrl");
            signupPageUrl = properties.getProperty("signupPageUrl");
            logoutPageUrl = properties.getProperty("logoutPageUrl");
            mainPageUrl = properties.getProperty("mainPageUrl");
            adminPageUrl = properties.getProperty("adminPageUrl");
            gameSocketUrl = properties.getProperty("gameSocketUrl");

        } catch (IOException e) {
            LOGGER.error(e);
            return;
        }

        if (port == null || host == null) {
            LOGGER.error("Port or host is null");
            throw new NullPointerException("Port or host is null");
        }

        if(signinPageUrl == null || signupPageUrl == null
                || logoutPageUrl == null || mainPageUrl == null
                || adminPageUrl == null || gameSocketUrl == null){
            LOGGER.error("Servlet url is null");
            throw new NullPointerException("Servlet Url is null");
        }

    }

    public static Configuration getInstance(){
        if(configuration == null){
            configuration = new Configuration();
        }
        return configuration;
    }

    public int getPort(){
        return new Integer(port);
    }

    public String getHost(){
        return host;
    }


    public String getSigninPageUrl(){
        return signinPageUrl;
    }

    public String getSignupPageUrl(){
        return signupPageUrl;
    }

    public String getMainPageUrl(){
        return mainPageUrl;
    }

    public String getLogoutPageUrl(){
        return logoutPageUrl;
    }

    public String getAdminPageUrl(){
        return adminPageUrl;
    }

    public String getGameSocketUrl(){
        return  gameSocketUrl;
    }
}
