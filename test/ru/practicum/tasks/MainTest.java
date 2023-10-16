package ru.practicum.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.server.HttpTaskServer;
import ru.practicum.tasks.manager.server.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class MainTest {
    private Main main;
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void setUp() {
        main = new Main();
        kvServer = mock(KVServer.class);
        httpTaskServer = mock(HttpTaskServer.class);
    }

    @Test
    public void testMainWhenServersStartThenSuccess() {
        assertDoesNotThrow(() -> {
            main.main(new String[]{});
        });
    }

    @Test
    public void testMainWhenKVServerFailsToStartThenHttpTaskServerStarts() throws IOException {
        doThrow(IOException.class).when(kvServer).start();

        assertDoesNotThrow(() -> {
            main.main(new String[]{});
        });
    }

    @Test
    public void testMainWhenHttpTaskServerFailsToStartThenKVServerStarts() throws IOException {
        doThrow(IOException.class).when(httpTaskServer).start();

        assertDoesNotThrow(() -> {
            main.main(new String[]{});
        });
    }
}