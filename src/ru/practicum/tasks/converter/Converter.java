package ru.practicum.tasks.converter;

import ru.practicum.tasks.manager.HistoryManager;
import ru.practicum.tasks.model.TypeOfTask;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    private Converter() {

    }

    public static String taskToString(Task task) {
        if (task != null) {
            return String.format("%d,%s,%s,%s,%s,", task.getId(),
                    typeOfTaskToString(task.getType()), task.getName(),
                    typeOfStatusToString(task.getStatus()), task.getDescriptions());
        }
        return "";
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder viewedIds = new StringBuilder();
        for (Task task : manager.getHistory()) {
            viewedIds.append(task.getId()).append(",");
        }
        return viewedIds.toString();
    }

    public static Task stringToTask(String line) {
        if (!line.equals(" ") && !line.equals("")) {
            String[] tokens = line.split(",");
            TypeOfTask type = TypeOfTask.valueOf(tokens[1]);
            for (int i = 0; i < tokens.length; i++) {
                switch (type) {
                    case TASK:
                        Task task = new Task();
                        task.setId(Integer.parseInt(tokens[0]));
                        task.setName(tokens[2]);
                        task.setStatus(Status.valueOf(tokens[3]));
                        task.setDescriptions(tokens[4]);
                        return task;
                    case EPIC:
                        Epic epic = new Epic();
                        epic.setId(Integer.parseInt(tokens[0]));
                        epic.setName(tokens[2]);
                        epic.setStatus(Status.valueOf(tokens[3]));
                        epic.setDescriptions(tokens[4]);
                        return epic;
                    case SUBTASK:
                        if (tokens.length > 4) {
                            Subtask subtask = new Subtask();
                            subtask.setId(Integer.parseInt(tokens[0]));
                            subtask.setName(tokens[2]);
                            subtask.setStatus(Status.valueOf(tokens[3]));
                            subtask.setDescriptions(tokens[4]);
                            subtask.setEpicId(Integer.parseInt(tokens[5]));
                            return subtask;
                        } else {
                            break;
                        }
                }
            }
        }
        return null;
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

    public static String typeOfTaskToString(TypeOfTask type) {
        for (TypeOfTask value : TypeOfTask.values()) {
            if (type.equals(value)) {
                return type.toString();
            }
        }
        return "";
    }

    public static String typeOfStatusToString(Status status) {
        for (Status value : Status.values()) {
            if (status.equals(value)) {
                return status.toString();
            }
        }
        return "";
    }
}
