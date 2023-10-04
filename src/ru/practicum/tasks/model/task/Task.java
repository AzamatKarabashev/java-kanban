package ru.practicum.tasks.model.task;

import ru.practicum.tasks.manager.Managers;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.TypeOfTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {

    protected String name;

    protected String description;

    protected Integer id;

    protected Status status;

    protected LocalDateTime startTime;

    protected Duration duration;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task() {

    }

    public TypeOfTask getType() {
        return TypeOfTask.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return "Task{" + "taskName='" + name + '\'' + ", taskDesc='" + description + '\'' + ", uniqueId=" + id + ", status=" + status + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        if (Managers.getDefaultInMemory().getPrioritizedTasks().isEmpty()) {
            this.startTime = startTime;
            return;
        }
        for (Task task : Managers.getDefaultInMemory().getPrioritizedTasks()) {
            if (task.startTime == null || task.getEndTime().isEmpty()) {
                this.startTime = startTime;
                return;
            }
            if (startTime.isBefore(task.startTime) || startTime.isAfter(task.getEndTime().get())) {
                try {
                    this.startTime = startTime;
                    return;
                } catch (RuntimeException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getEndTime() {
        return Optional.of(startTime.plus(duration));
    }
}
