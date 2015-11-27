package resourceSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.SaxHandler;
import utils.VFS;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ilya on 01.11.15.
 */
public class ResourceFactory {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(ResourceFactory.class);

    private static ResourceFactory resourceFactory;

    private ResourceFactory() {
    }

    @NotNull
    public static ResourceFactory instance() {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }

    @Nullable
    public Resource get(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SaxHandler handler = new SaxHandler();
            saxParser.parse(xmlFile, handler);

            return (Resource)handler.getObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static Map<Class<?>, Resource> loadResources(@NotNull String resourcesDirectory) {
        Map<Class<?>, Resource> resources = new HashMap<>();
        VFS vfs = new VFS();
        Iterator<String> iter = vfs.getIterator(resourcesDirectory);
        while (iter.hasNext()) {
            String fileName = iter.next();
            System.out.println(fileName);
            if (fileName != null && fileName.contains(".xml")) {
                Resource resource = ResourceFactory.instance().get(fileName);
                if (resource != null && resource.getClass() != null) {
                    resources.put(resource.getClass(), resource);
                } else {
                    LOGGER.error("Fail to create resource class");
                }
            }
        }
        return resources;
    }
}
