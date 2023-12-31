package ru.practicum.tasks.model.task;

import ru.practicum.tasks.model.TypeOfTask;
import ru.practicum.tasks.model.Status;

import java.util.Objects;

public class Subtask extends Task {

    private Integer epicId;

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask () {

    }


    @Override
    public TypeOfTask getType () {
        return TypeOfTask.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", taskName='" + name + '\'' +
                ", taskDesc='" + description + '\'' +
                ", uniqueId=" + id +
                ", status=" + status +
                '}';
    }
}
