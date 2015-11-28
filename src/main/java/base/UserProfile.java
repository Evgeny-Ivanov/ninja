package base;

import org.jetbrains.annotations.NotNull;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {
    @NotNull
    private final String name;
    @NotNull
    private final String password;
    @NotNull
    private final String email;

    public UserProfile(@NotNull String name, @NotNull String password, @NotNull String email) {
        this.name = name;
        this.password = password;
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
