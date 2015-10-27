package base;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ilya on 27.10.15.
 */
public class UrlParameters {
    @NotNull
    private String host;
    @NotNull
    private String port;
    @NotNull
    private String socketUrl;

    public UrlParameters(@NotNull String host,
                         @NotNull String port,
                         @NotNull String socketUrl) {
        this.host = host;
        this.port = port;
        this.socketUrl = socketUrl;

    }

    @NotNull
    public String getHost() {
        return host;
    }

    public void setHost(@NotNull String host) {
        this.host = host;
    }

    @NotNull
    public String getPort() {
        return port;
    }

    public void setPort(@NotNull String port) {
        this.port = port;
    }

    @NotNull
    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSSocketUrl(@NotNull String socketUrl) {
        this.socketUrl = socketUrl;
    }
}
