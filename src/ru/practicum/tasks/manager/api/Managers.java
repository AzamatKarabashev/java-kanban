package ru.practicum.tasks.manager.api;

import ru.practicum.tasks.manager.impl.HttpTaskManager;
import ru.practicum.tasks.manager.impl.InMemoryHistoryManager;

import java.io.IOException;

public final class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() {
        };
    }

    public static TaskManager getDefault() throws IOException {
        return new HttpTaskManager() {
        };
    }
}
