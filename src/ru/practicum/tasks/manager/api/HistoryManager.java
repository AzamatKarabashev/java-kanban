package ru.practicum.tasks.manager.api;

import ru.practicum.tasks.model.task.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);

    void remove(int id);
}
