package ru.practicum.tasks.manager.api;

import ru.practicum.tasks.manager.imp.InMemoryHistoryManager;

public final class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() {
        };
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager() {
        };
    }
}
