package ru.practicum.tasks.manager;

import ru.practicum.tasks.manager.exceptions.ManagerSaveException;
import ru.practicum.tasks.manager.files.Converter;
import ru.practicum.tasks.manager.taskModul.TypeOfTask;
import ru.practicum.tasks.statusModul.Status;
import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static ru.practicum.tasks.manager.Managers.getDefault;
import static ru.practicum.tasks.manager.Managers.getDefaultHistory;
import static ru.practicum.tasks.manager.files.Converter.historyToString;
import static ru.practicum.tasks.manager.files.Converter.taskToString;

public class FileBackedTasksManager extends InMemoryTaskManager {

    /*
    Для этого создайте метод static void main(String[] args)
    в классе FileBackedTasksManager и реализуйте небольшой сценарий:
    */

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
        getSubtaskById(subtask1Id);
        createAndSaveFile();


//      Создайте новый FileBackedTasksManager менеджер из этого же файла.
//      Проверьте, что история просмотра восстановилась верно и все задачи,
//      эпики, подзадачи, которые были в старом, есть в новом менеджере.

        String path = "C:\\Users\\Mugen\\IdeaProjects\\CSVFileTest\\COMMA-COMMA.csv";
        File file = new File(path);
        loadAndRestoreFromFile(file);
    }

    public static void main(String[] args) {
        new FileBackedTasksManager().test();
    }

    public FileBackedTasksManager() {

    }

    public FileBackedTasksManager(File file) {

    }

    public static FileBackedTasksManager loadAndRestoreFromFile(File file) {
        FileBackedTasksManager restoredFromFileManager = new FileBackedTasksManager(file);
        String path = "C:\\Users\\Mugen\\IdeaProjects\\CSVFileTest\\COMMA-COMMA.csv";
        try {
            List<String> fileContent = Files.readAllLines(Path.of(path));
            for (String line : fileContent) {
                if (!line.isBlank()) {
                    if (Objects.requireNonNull(Converter.stringToTask(line)).getType().equals(TypeOfTask.TASK)) {
                        restoredFromFileManager.addNewTask(Converter.stringToTask(line));
                    } else if (Objects.requireNonNull(Converter.stringToTask(line)).getType().equals(TypeOfTask.EPIC)) {
                        restoredFromFileManager.addNewEpic((Epic) Converter.stringToTask(line));
                    } else {
                        Subtask subtask = new Subtask();
                        int epicId = 0;
                        String[] partsOfLine = line.split(",");
                        for (int i = 0; i < partsOfLine.length; i++) {
                            subtask.setUniqueId(Integer.parseInt(partsOfLine[0]));
                            subtask.setTaskName(partsOfLine[2]);
                            Status status = Status.valueOf(partsOfLine[3]);
                            subtask.setStatus(status);
                            subtask.setTaskDesc(partsOfLine[4]);
                            epicId = Integer.parseInt(partsOfLine[5]);
                        }
                        restoredFromFileManager.addNewSubtask((Subtask) Converter.stringToTask(line),
                                restoredFromFileManager.getEpicById(epicId));
                    }
                } else {
                    convertRestoredListOfHistoryInHistoryManager(Converter.historyFromStringToList(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return restoredFromFileManager;
    }

    private void createAndSaveFile() {
        String path = "C:\\Users\\Mugen\\IdeaProjects\\CSVFileTest\\COMMA-COMMA.csv";
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

    private static void convertRestoredListOfHistoryInHistoryManager(List<Integer> listOfRestoredHistory) {
        if (!listOfRestoredHistory.isEmpty()) {
            TaskManager taskManager = getDefault();
            HistoryManager historyManager = getDefaultHistory();
            for (Integer integer : listOfRestoredHistory) {
                for (Task task : taskManager.getTasks()) {
                    if (task.getUniqueId().equals(integer)) {
                        historyManager.add(task);
                    }
                }
                for (Epic epic : taskManager.getEpics()) {
                    if (epic.getUniqueId().equals(integer)) {
                        historyManager.add(epic);
                    }
                }
                for (Subtask subtask : taskManager.getSubtasks()) {
                    if (subtask.getUniqueId().equals(integer)) {
                        historyManager.add(subtask);
                    }
                }
            }
        }
    }

    @Override
    public Integer addNewTask(Task task) {
        Integer id = super.addNewTask(task);
        createAndSaveFile();
        return id;
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        Integer id = super.addNewEpic(epic);
        createAndSaveFile();
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask, Epic epic) {
        Integer id = super.addNewSubtask(subtask, epic);
        createAndSaveFile();
        return id;
    }
}