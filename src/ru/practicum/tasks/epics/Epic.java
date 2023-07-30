package ru.practicum.tasks.epics;

import ru.practicum.tasks.status.Status;
import ru.practicum.tasks.task.Task;


public class Epic extends Task {
    public Epic(String taskName, String taskDesc, Status status) {
        super(taskName, taskDesc, status);
    }

    public Epic() {
    }
}
