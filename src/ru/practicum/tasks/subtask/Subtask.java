package ru.practicum.tasks.subtask;

import ru.practicum.tasks.status.Status;
import ru.practicum.tasks.task.Task;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String taskName, String taskDesc, Status status, Integer epicId) {
        super(taskName, taskDesc, status);
        this.epicId = epicId;
    }

    public Subtask() {
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}
