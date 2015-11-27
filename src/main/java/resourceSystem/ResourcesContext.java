package resourceSystem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

/**
 * Created by ilya on 27.11.15.
 */
public class ResourcesContext {
    @NotNull
    private Map<Class<?>, Resource> context;

    private ResourcesContext() {
    }

    public ResourcesContext(@NotNull String dir) {
        context = ResourceFactory.loadResources(dir);
    }

    public void add(Class<?> clazz, Resource resource){
        if(context.containsKey(clazz)){
            throw new RuntimeException();
        }
        context.put(clazz, resource);
    }

    @Nullable
    public Resource get(Class<?> clazz){
        return context.get(clazz);
    }
}
