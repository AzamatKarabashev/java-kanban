package ru.practicum.tasks.manager;

import ru.practicum.tasks.manager.exceptions.ManagerSaveException;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static ru.practicum.tasks.converter.Converter.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    private final String path = "COMMA-COMMA.csv";

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void test() {
        System.out.println("Тест нового менеджера ТЗ-6: \n");

//      Заведите несколько разных задач, эпиков и подзадач.

        Task task1 = new Task("Такс1", "Описание таск1", Status.NEW);
        int task1Id = addNewTask(task1);

        Epic epic1 = new Epic("Эпик1", "Описание эпик1", Status.NEW);
        int epic1Id = addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаск1", "Описание сабтаск1", Status.DONE, epic1Id);
        int subtask1Id = addNewSubtask(subtask1, epic1);

        Subtask subtask2 = new Subtask("Сабтаск2", "Описание сабтаск2", Status.DONE, epic1Id);
        int subtask2Id = addNewSubtask(subtask2, epic1);

//      Запросите некоторые из них, чтобы заполнилась история просмотра.

        getTaskById(task1Id);
        getEpicById(epic1Id);
        getSubtaskById(subtask2Id);
        save();

//      Создайте новый FileBackedTasksManager менеджер из этого же файла.
//      Проверьте, что история просмотра восстановилась верно и все задачи,
//      эпики, подзадачи, которые были в старом, есть в новом менеджере.

        File file = new File(path);

        FileBackedTasksManager testRestoredOne = restoreFromFile(file);
        System.out.println("\nТест работы: история просмотра \n");
        for (Task task : testRestoredOne.inMemoryHistoryManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("\nТест работы: список тасок \n");
        for (Task task : testRestoredOne.getTasks()) {
            System.out.println(task);
        }
        System.out.println("\nТест работы: список эпиков \n");
        for (Epic epic : testRestoredOne.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("\nТест работы: список сабтасок \n");
        for (Subtask subtask : testRestoredOne.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    public static void main(String[] args) {
        new FileBackedTasksManager().test();
    }

    public static FileBackedTasksManager restoreFromFile(File file) {
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try {
            String csv = Files.readString(file.toPath());
            String[] lines = csv.split("\n");
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (!line.isEmpty()) {
                    Task task = stringToTask(line);
                    if (task != null) {
                        switch (task.getType()) {
                            case TASK:
                                taskManager.addNewTask(task);
                                break;
                            case EPIC:
                                Epic epic = (Epic) stringToTask(line);
                                taskManager.addNewEpic(epic);
                                break;
                            case SUBTASK:
                                Subtask subtask = (Subtask) stringToTask(line);
                                Epic subtasksEpic = taskManager.getEpicById(subtask.getEpicId());
                                if (subtasksEpic != null) {
                                    taskManager.addNewSubtask(subtask, subtasksEpic);
                                }
                                break;
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

    private void save() {
        try (FileWriter csvOutputFile = new FileWriter(path)) {
            csvOutputFile.write("id,type,name,status,description,epic\n");
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

    private static void convertRestoredListOfHistoryInHistoryManager(List<Integer> restoredHistory,
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
}
