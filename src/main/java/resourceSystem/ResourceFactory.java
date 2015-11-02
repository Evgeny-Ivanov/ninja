package resourceSystem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.SaxHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by ilya on 01.11.15.
 */
public class ResourceFactory {
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
    public Object get(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SaxHandler handler = new SaxHandler();
            saxParser.parse(xmlFile, handler);

            return handler.getObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
