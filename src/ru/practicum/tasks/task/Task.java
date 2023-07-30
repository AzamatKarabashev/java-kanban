package ru.practicum.tasks.task;

import ru.practicum.tasks.status.Status;

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
    public boolean equals(Object obj) {
        return super.equals(obj);
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
