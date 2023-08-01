package ru.practicum.tasks.task;

import ru.practicum.tasks.model.Status;

import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDesc;
    protected Integer uniqueId;
    protected Status status;

    public Task(String taskName, String taskDesc, Status status) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.status = status;
    }

    public Task() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDesc, task.taskDesc)
                && Objects.equals(uniqueId, task.uniqueId) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDesc, uniqueId, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", uniqueId=" + uniqueId +
                ", status=" + status +
                '}';
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
