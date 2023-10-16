package ru.practicum.tasks.manager.imp;

import ru.practicum.tasks.manager.exception.ManagerSaveException;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static ru.practicum.tasks.converter.CSVConverter.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    private final String path = "COMMA-COMMA.csv";

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager restoreFromFile(File file) {
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try {
            String csv = Files.readString(file.toPath());
            String[] lines = csv.split("\n");
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (!line.isEmpty()) {
                    if (stringToTask(line).isPresent()) {
                        Task task = stringToTask(line).get();
                        switch (task.getType()) {
                            case TASK -> taskManager.addNewTask(task);
                            case EPIC -> {
                                Epic epic = (Epic) stringToTask(line).get();
                                taskManager.addNewEpic(epic);
                            }
                            case SUBTASK -> {
                                Subtask subtask = (Subtask) stringToTask(line).get();
                                Epic subtasksEpic = taskManager.getEpicById(subtask.getEpicId());
                                taskManager.addNewSubtask(subtask, subtasksEpic);
                            }
                        }
                    }
                } else {
                    List<Integer> history = historyFromStringToList(lines[i + 1]);
                    convertRestoredListOfHistoryInHistoryManager(history, taskManager);
                    break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return taskManager;
    }

    void save() {
        try (FileWriter csvOutputFile = new FileWriter(path)) {
            csvOutputFile.write("id,type,name,status,description,startTime,duration,epic\n");
            for (Task task : getTasks()) {
                csvOutputFile.write(taskToString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                csvOutputFile.write(taskToString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                csvOutputFile.write(taskToString(subtask) + subtask.getEpicId() + "\n");
            }
            csvOutputFile.write("\n");
            csvOutputFile.write(historyToString(inMemoryHistoryManager));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static void convertRestoredListOfHistoryInHistoryManager(List<Integer> restoredHistory,
                                                                     FileBackedTasksManager manager) {
        if (!restoredHistory.isEmpty()) {
            for (Integer integer : restoredHistory) {
                for (Task task : manager.getTasks()) {
                    if (task.getId().equals(integer)) {
                        manager.inMemoryHistoryManager.add(task);
                    }
                }
                for (Epic epic : manager.getEpics()) {
                    if (epic.getId().equals(integer)) {
                        manager.inMemoryHistoryManager.add(epic);
                    }
                }
                for (Subtask subtask : manager.getSubtasks()) {
                    if (subtask.getId().equals(integer)) {
                        manager.inMemoryHistoryManager.add(subtask);
                    }
                }
            }
        }
    }

    @Override
    public Task getTaskById(Integer uniqueId) {
        Task task = super.getTaskById(uniqueId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer uniqueId) {
        Epic epic = super.getEpicById(uniqueId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer uniqueId) {
        Subtask subtask = super.getSubtaskById(uniqueId);
        save();
        return subtask;
    }

    @Override
    public void removeTaskById(Integer uniqueId) {
        super.removeTaskById(uniqueId);
        save();
    }

    @Override
    public void removeEpicById(Integer uniqueId) {
        super.removeEpicById(uniqueId);
        save();
    }

    @Override
    public void removeSubtaskById(Integer uniqueId) {
        super.removeSubtaskById(uniqueId);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public Integer addNewTask(Task task) {
        Integer id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        Integer id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask, Epic epic) {
        Integer id = super.addNewSubtask(subtask, epic);
        save();
        return id;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
