package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyOfViews;

    public InMemoryHistoryManager() {
        historyOfViews = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyOfViews);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        historyOfViews.add(task);
        if (historyOfViews.size() >= 10) {
            historyOfViews.remove(0);
        }
    }
}
