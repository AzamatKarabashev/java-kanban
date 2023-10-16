package ru.practicum.tasks.manager.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ru.practicum.tasks.converter.*;
import ru.practicum.tasks.manager.server.KVTaskClient;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String TASK = "task";
    private static final String EPIC = "epic";
    private static final String SUBTASK = "subtask";
    private static final String HISTORY = "history";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskSerializer())
            .registerTypeAdapter(Epic.class, new EpicSerializer())
            .registerTypeAdapter(Subtask.class, new SubtaskSerializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
            .serializeNulls()
            .create();

    private final KVTaskClient kvTaskClient;

    public HttpTaskManager() throws IOException {
        super();
        kvTaskClient = new KVTaskClient();
        restoreFromLocalHost();
    }

    private void restoreFromLocalHost() {
        try {
            String tasksJson = kvTaskClient.load(TASK);
            List<Task> tasksList = gson.fromJson(tasksJson, new TypeToken<ArrayList<Task>>() {
            });
            if (tasksList != null) {
                tasksList.forEach(t -> {
                    tasks.add(t);
                    prioritizedTasks.add(t);
                });
            }

            String epicsJson = kvTaskClient.load(EPIC);
            List<Epic> epicsList = gson.fromJson(epicsJson, new TypeToken<ArrayList<Epic>>() {
            });
            if (epicsList != null) {
                epicsList.forEach(epic -> {
                    epics.add(epic);
                    prioritizedTasks.add(epic);
                });
            }

            String subtasksJson = kvTaskClient.load(SUBTASK);
            List<Subtask> subTaskList = gson.fromJson(subtasksJson, new TypeToken<ArrayList<Subtask>>() {
            });
            if (subTaskList != null) {
                subTaskList.forEach(st -> {
                    subtasks.add(st);
                    prioritizedTasks.add(st);
                });
            }

            String historyJson = kvTaskClient.load(HISTORY);
            List<Task> restoreHistory = gson.fromJson(historyJson, new TypeToken<ArrayList<Task>>() {
            });
            if (restoreHistory != null) {
                restoreHistory.forEach(inMemoryHistoryManager::add);
            }
            combineEpicPlusSubtask();
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void save() {
        try {
            kvTaskClient.put(TASK, gson.toJson(tasks));
            kvTaskClient.put(EPIC, gson.toJson(epics));
            kvTaskClient.put(SUBTASK, gson.toJson(subtasks));
            kvTaskClient.put(HISTORY, gson.toJson(inMemoryHistoryManager.getHistory()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void combineEpicPlusSubtask() {
        if (!this.epics.isEmpty()) {
            if (!this.subtasks.isEmpty()) {
                for (Epic epic : epics) {
                    for (Subtask subtask : subtasks) {
                        if (Objects.equals(subtask.getEpicId(), epic.getId())) {
                            epic.getSubtaskIds().add(subtask.getId());
                        }
                    }
                }
            }
        }
    }
}