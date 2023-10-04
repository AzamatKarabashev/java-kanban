package ru.practicum.tasks.manager;

import ru.practicum.tasks.model.task.Task;

import java.util.Comparator;

public class TaskLocalDateTimeSorter implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime() == null || o2.getStartTime() == null) {
            return o1.getStatus().compareTo(o2.getStatus());
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    }
}
