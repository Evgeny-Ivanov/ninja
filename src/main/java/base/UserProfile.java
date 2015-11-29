package base;

import org.jetbrains.annotations.NotNull;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {
    @NotNull
    private String name;
    @NotNull
    private String password;
    @NotNull
    private String email;

    public UserProfile(@NotNull String name, @NotNull String password, @NotNull String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UserProfile() {
        this.name = "";
        this.password = "";
        this.email = "";
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

}
