package ru.practicum.tasks.model.task;

import ru.practicum.tasks.model.TypeOfTask;
import ru.practicum.tasks.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic() {
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }

    public void addSubtaskIdToList(Integer id) {
        subtaskIds.add(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + name + '\'' +
                ", taskDesc='" + description + '\'' +
                ", uniqueId=" + id +
                ", status=" + status +
                '}';
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
}
