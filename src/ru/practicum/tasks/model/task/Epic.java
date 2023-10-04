package ru.practicum.tasks.model.task;

import ru.practicum.tasks.manager.Managers;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.TypeOfTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private LocalDateTime endTime;

    private final List<Integer> subtaskIds = new ArrayList<>();

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

    public void calculateStartTimeForEpic() {
        for (Subtask subtask : Managers.getDefaultInMemory().getEpicSubtasksByEpicId(id)) {
            if (subtask.startTime != null) {
                if (startTime.isBefore(subtask.startTime)) {
                    startTime = subtask.startTime;
                }
            }
        }
    }

    public void calculateDurationTimeForEpic() {
        for (Subtask subtask : Managers.getDefaultInMemory().getEpicSubtasksByEpicId(id)) {
            if (duration != null && subtask.duration != null) {
                duration.plus(subtask.duration);
            }
        }
    }

    public void calculateEndTimeForEpic() {
        for (Subtask subtask : Managers.getDefaultInMemory().getEpicSubtasksByEpicId(id)) {
            if (subtask.getEndTime().isPresent()) {
                if (endTime != null) {
                    if (endTime.isAfter(subtask.getEndTime().get())) {
                        endTime = subtask.getEndTime().get();
                    }
                }
            }
        }
    }
}
