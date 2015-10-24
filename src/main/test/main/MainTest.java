package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by ilya on 23.10.15.
 */
public class MainTest extends Mockito{
    static final int DEFAULT_PORT = 8080;

    private final Server server = mock(Server.class);

    @Test
    public void testMain() throws Exception {
        //Main.main(new String[]{"8001"});

        //noinspection ConstantConditions
        //verify(server, atLeastOnce()).setHandler(mock(HandlerList.class));
        //noinspection ConstantConditions
        //verify(server, atLeastOnce()).start();



    }
}