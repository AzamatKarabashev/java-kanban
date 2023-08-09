package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);
}
