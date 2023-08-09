package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final Integer LIMIT_OF_HISTORY_SIZE = 10;

    private final List<Task> historyOfViews;

    public InMemoryHistoryManager() {
        historyOfViews = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyOfViews);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        historyOfViews.add(task);
        if (historyOfViews.size() >= LIMIT_OF_HISTORY_SIZE) {
            historyOfViews.remove(0);
        }
    }
}
