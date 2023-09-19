package ru.practicum.tasks.task;


import ru.practicum.tasks.manager.taskModul.TypeOfTask;
import ru.practicum.tasks.statusModul.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subtasksIdList;

    public Epic(String taskName, String taskDesc, Status status) {
        super(taskName, taskDesc, status);
        subtasksIdList = new ArrayList<>();
    }

    public Epic() {
    }

    @Override
    public TypeOfTask getType () {
        return TypeOfTask.EPIC;
    }

    public void addSubtaskIdToList(Integer uniqueId) {
        subtasksIdList.add(uniqueId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksIdList, epic.subtasksIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIdList);
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

    public List<Integer> getSubtasksIdList() {
        return subtasksIdList;
    }

    public void setSubtasksIdList(List<Integer> subtasksIdList) {
        this.subtasksIdList = subtasksIdList;
    }
}
