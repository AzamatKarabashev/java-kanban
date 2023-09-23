package ru.practicum.tasks.task;

import ru.practicum.tasks.model.TypeOfTask;
import ru.practicum.tasks.model.Status;

import java.util.Objects;

public class Task {
    protected String name;
    protected String descriptions;
    protected Integer id;
    protected Status status;

    public Task(String name, String descriptions, Status status) {
        this.name = name;
        this.descriptions = descriptions;
        this.status = status;
    }

    public Task() {
    }

    public TypeOfTask getType (){
        return TypeOfTask.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(descriptions, task.descriptions)
                && Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, descriptions, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + name + '\'' +
                ", taskDesc='" + descriptions + '\'' +
                ", uniqueId=" + id +
                ", status=" + status +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
