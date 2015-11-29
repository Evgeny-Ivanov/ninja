package base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 27.11.15.
 */
public final class GameContext {
    @NotNull
    private static final GameContext INSTANCE = new GameContext();

    @NotNull
    private final Map<Class<?>, Object> context = new HashMap<>();

    private GameContext() {
    }

    public static GameContext getInstance() {
        return INSTANCE;
    }

    public void add(Class<?> clazz, Object object) {
        if (context.containsKey(clazz)) {
            throw new RuntimeException();
        }
        context.put(clazz, object);
    }

    @Nullable
    public Object get(Class<?> clazz) {
        return context.get(clazz);
    }
}
