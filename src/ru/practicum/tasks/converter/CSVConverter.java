package ru.practicum.tasks.converter;

import ru.practicum.tasks.manager.api.HistoryManager;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.TypeOfTask;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CSVConverter {

    private CSVConverter() {

    }

    public static String taskToString(Task task) {
        try {
            return String.format("%d,%s,%s,%s,%s,%s,%s,",
                    task.getId(),
                    task.getType(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getStartTime(),
                    task.getDuration());
        }
        catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder viewedIds = new StringBuilder();
        for (Task task : manager.getHistory()) {
            viewedIds.append(task.getId()).append(",");
        }
        return viewedIds.toString();
    }

    public static Optional<Task> stringToTask(String line) {
        try {
            if (!line.isBlank() && !line.isEmpty()) {
                String[] tokens = line.split(",");
                TypeOfTask type = TypeOfTask.valueOf(tokens[1]);
                for (int i = 0; i < tokens.length; i++) {
                    switch (type) {
                        case TASK -> {
                            Task task = new Task();
                            task.setId(Integer.parseInt(tokens[0]));
                            task.setName(tokens[2]);
                            task.setStatus(Status.valueOf(tokens[3]));
                            task.setDescription(tokens[4]);
                            if (!tokens[5].equals("null") && !tokens[6].equals("null")) {
                                task.setStartTime(LocalDateTime.parse(tokens[5]));
                                task.setDuration(Duration.parse(tokens[6]));
                            }
                            return Optional.of(task);
                        }
                        case EPIC -> {
                            Epic epic = new Epic();
                            epic.setId(Integer.parseInt(tokens[0]));
                            epic.setName(tokens[2]);
                            epic.setStatus(Status.valueOf(tokens[3]));
                            epic.setDescription(tokens[4]);
                            if (!tokens[5].equals("null") && !tokens[6].equals("null")) {
                                epic.setStartTime(LocalDateTime.parse(tokens[5]));
                                epic.setDuration(Duration.parse(tokens[6]));
                            }
                            return Optional.of(epic);
                        }
                        case SUBTASK -> {
                            if (tokens.length > 7) {
                                Subtask subtask = new Subtask();
                                subtask.setId(Integer.parseInt(tokens[0]));
                                subtask.setName(tokens[2]);
                                subtask.setStatus(Status.valueOf(tokens[3]));
                                subtask.setDescription(tokens[4]);
                                if (!tokens[5].equals("null") && !tokens[6].equals("null")) {
                                    subtask.setStartTime(LocalDateTime.parse(tokens[5]));
                                    subtask.setDuration(Duration.parse(tokens[6]));
                                }
                                subtask.setEpicId(Integer.parseInt(tokens[7]));

                                return Optional.of(subtask);
                            }
                        }
                    }
                }
            }
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        } return Optional.empty();
    }

    public static List<Integer> historyFromStringToList(String value) {
        List<Integer> listFilledWithIds = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] listOfViewed = value.split(",");
            for (String s : listOfViewed) {
                listFilledWithIds.add(Integer.parseInt(s));
            }
        }
        return listFilledWithIds;
    }
}
