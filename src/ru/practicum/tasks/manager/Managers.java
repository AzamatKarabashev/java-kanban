package ru.practicum.tasks.manager;

public final class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() {
        };
    }

    public static TaskManager getDefaultInMemory() {
        return new InMemoryTaskManager() {
        };
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager() {
        };
    }

}
