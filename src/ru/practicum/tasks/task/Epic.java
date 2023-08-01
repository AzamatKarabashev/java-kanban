package ru.practicum.tasks.task;


import ru.practicum.tasks.model.Status;

public class Epic extends Task {
    public Epic(String taskName, String taskDesc, Status status) {
        super(taskName, taskDesc, status);
    }

    public Epic() {
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", uniqueId=" + uniqueId +
                ", status=" + status +
                '}';
    }
}
