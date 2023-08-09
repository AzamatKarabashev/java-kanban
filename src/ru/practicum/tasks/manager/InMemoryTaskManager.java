package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.practicum.tasks.manager.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private List<Epic> epics = new ArrayList<>();
    private List<Subtask> subtasks = new ArrayList<>();
    private Integer id = 0;
    private final HistoryManager inMemoryHistoryManager = getDefaultHistory();

    @Override
    public void printHistory() {
        for (Task task : inMemoryHistoryManager.getHistory()) {
            System.out.println(task);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        System.out.println("Все задачи успешно удалены");
    }

    @Override
    public Task getTaskById(Integer uniqueId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Задача с ID: " + uniqueId + " найдена!");
                inMemoryHistoryManager.add(task);//добавили сохранение в историю просмотров
                return task;
            }
        }
        return null;
    }

    @Override
    public Epic getEpicById(Integer uniqueId) {
        for (Epic epic : epics) {
            if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Эпик с ID: " + uniqueId + " найден!");
                inMemoryHistoryManager.add(epic);//добавили сохранение в историю просмотров
                return epic;
            }
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Integer uniqueId) {
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Подзадача с ID: " + uniqueId + " найдена!");
                inMemoryHistoryManager.add(subtask);//добавили сохранение в историю просмотров
                return subtask;
            }
        }
        return null;
    }

    @Override
    public void removeTask(Integer uniqueId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                tasks.remove(task);
                System.out.println("Задача с ID: " + uniqueId + " удалена!");
                return;
            }
        }
    }

    @Override
    public void removeEpic(Integer uniqueId) {
        for (Epic epic : epics) {
            if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                epics.remove(epic);
                System.out.println("Эпик с ID: " + uniqueId + " удален!");
                subtasks.removeIf(subtask -> Objects.equals(subtask.getEpicId(), uniqueId));
                return;
            }
        }
    }

    @Override
    public void removeSubtask(Integer uniqueId) {
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                subtasks.remove(subtask);
                System.out.println("Подзадача с ID: " + uniqueId + " удалена!");
                updateStatus(subtask.getEpicId());
                return;
            }
        }
    }

    @Override
    public List<Subtask> getEpicsSubtasks(Integer uniqueId) {
        List<Subtask> epicsSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getEpicId(), uniqueId)) {
                epicsSubtasks.add(subtask);
            }
        }
        return epicsSubtasks;
    }

    @Override
    public Integer addNewTask(Task task) {
        if (task == null) {
            System.out.println("Задачу невозможно сохранить");
            return null;
        }
        task.setUniqueId(generateId());
        tasks.add(task);
        return task.getUniqueId();
    }

    @Override
    public void updateTask(Task newTask) {
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (Objects.equals(task.getUniqueId(), newTask.getUniqueId())) {
                    Collections.replaceAll(tasks, task, newTask);
                    System.out.println("Задача обновлена");
                    return;
                }
            }
        }
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Эпик невозможно сохранить");
            return null;
        }
        epic.setUniqueId(generateId());
        epics.add(epic);
        return epic.getUniqueId();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (!epics.isEmpty()) {
            for (Epic epic : epics) {
                if (Objects.equals(epic.getUniqueId(), newEpic.getUniqueId())) {
                    Collections.replaceAll(epics, epic, newEpic);
                    System.out.println("Эпик обновлен");
                    return;
                }
            }
        }
    }

    @Override
    public Integer addNewSubtask(Subtask subtask, Epic epic) {
        int epicId = epics.indexOf(epic);
        Epic epicL = epics.get(epicId);
        subtask.setUniqueId(generateId());
        subtasks.add(subtask);
        updateStatus(subtask.getEpicId());
        epicL.addSubtaskIdToList(subtask.getUniqueId());
        return subtask.getUniqueId();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        List<Subtask> checkEpic = getEpicsSubtasks(newSubtask.getEpicId());
        if (checkEpic.isEmpty()) {
            System.out.println("Эпика к котрому вы хотите создать подзадачу нет");
            return;
        }
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (Objects.equals(subtask.getUniqueId(), newSubtask.getUniqueId())) {
                    Collections.replaceAll(subtasks, subtask, newSubtask);
                    System.out.println("Подзадача обновлена");
                    updateStatus(subtask.getEpicId());
                    return;
                }
            }
        }
    }

    @Override
    //Создание Id.
    public Integer generateId() {
        return id++;
    }

    @Override
    public void updateStatus(Integer uniqueId) {
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                List<Subtask> epicSubtasks = getEpicsSubtasks(uniqueId);
                if (epicSubtasks.isEmpty()) {
                    epic.setStatus(Status.NEW);
                    return;
                }
                for (Subtask epicSubtask : epicSubtasks) {
                    if (epicSubtask.getStatus() == Status.NEW) {
                        epic.setStatus(Status.NEW);
                    } else if (epicSubtask.getStatus() == Status.IN_PROGRESS && epicSubtask.getStatus() != Status.NEW
                            && epicSubtask.getStatus() != Status.DONE) {
                        epic.setStatus(Status.IN_PROGRESS);
                    } else if (epicSubtask.getStatus() == Status.DONE && epicSubtask.getStatus() != Status.IN_PROGRESS
                            && epicSubtask.getStatus() != Status.NEW) {
                        epic.setStatus(Status.DONE);
                    }
                }
            }
        }
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public List<Epic> getEpics() {
        return epics;
    }

    @Override
    public void setEpics(List<Epic> epics) {
        this.epics = epics;
    }

    @Override
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
