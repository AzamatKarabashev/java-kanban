package ru.practicum.tasks;

import ru.practicum.tasks.manager.impl.HttpTaskManager;
import ru.practicum.tasks.manager.server.HttpTaskServer;
import ru.practicum.tasks.manager.server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskManager manager = new HttpTaskManager();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();

    }
}
