package ru.practicum.tasks.manager.files;

import ru.practicum.tasks.manager.HistoryManager;
import ru.practicum.tasks.manager.taskModul.TypeOfTask;
import ru.practicum.tasks.statusModul.Status;
import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    private Converter() {

    }

    public static String taskToString(Task task) {
        if (task != null) {
            return String.format("%d,%s,%s,%s,%s,", task.getUniqueId(),
                    typeOfTaskToString(task.getType()), task.getTaskName(),
                    typeOfStatusToString(task.getStatus()), task.getTaskDesc());
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder viewedIds = new StringBuilder();
        if (!manager.getHistory().isEmpty()) {
            for (Task task : manager.getHistory()) {
                viewedIds.append(task.getUniqueId()+1).append(",");
            }
        }
        return viewedIds.toString();
    }

    public static Task stringToTask(String text) {
        Task task = new Task();
        String[] partsOfText = text.split(",");
        for (int i = 0; i < partsOfText.length; i++) {
            task.setUniqueId(Integer.parseInt(partsOfText[0]));
            task.setTaskName(partsOfText[2]);
            Status status = Status.valueOf(partsOfText[3]);
            task.setStatus(status);
            task.setTaskDesc(partsOfText[4]);
        }
        if (task == null) {
            System.out.println("таску не получилось заполнить");
            return null;
        }
        return task;
    }

    public static List<Integer> historyFromStringToList(String value) {
        List<Integer> listFilledWithIds = new ArrayList<>();
        String[] listOfViewed = value.split(",");
        for (String s : listOfViewed) {
            listFilledWithIds.add(Integer.parseInt(s));
        }
        return listFilledWithIds;
    }

    public static String typeOfTaskToString(TypeOfTask type) {
        for (TypeOfTask value : TypeOfTask.values()) {
            if (type.equals(value)) {
                return type.toString();
            }
        }
        return null;
    }

    public static String typeOfStatusToString(Status status) {
        for (Status value : Status.values()) {
            if (status.equals(value)) {
                return status.toString();
            }
        }
        return null;
    }
}
