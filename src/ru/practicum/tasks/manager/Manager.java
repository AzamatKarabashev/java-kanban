package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Manager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Epic> epics = new ArrayList<>(10);
    private ArrayList<Subtask> subtasks = new ArrayList<>();
    private Integer id = 0;

    //Удаление всех задач.
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        System.out.println("Все задачи успешно удалены");
    }

    //Получение по идентификатору.
    public Task getTaskById(Integer uniqueId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Задача с ID: " + uniqueId + " найдена!");
                return task;
            }
        }
        return null;
    }

    public Epic getEpicById(Integer uniqueId) {
        for (Epic epic : epics) {
            if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Эпик с ID: " + uniqueId + " найден!");
                return epic;
            }
        }
        return null;
    }

    public Subtask getSubtaskById(Integer uniqueId) {
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Подзадача с ID: " + uniqueId + " найдена!");
                return subtask;
            }
        }
        return null;
    }

    //Удаление по идентификатору.
    public void removeTaskById(Integer uniqueId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Задача с ID: " + uniqueId + " удалена!");
                tasks.remove(task);
                return;
            }
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Эпик с ID: " + uniqueId + " удален!");
                epics.remove(epic);
                return;
            }
        }
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getUniqueId(), uniqueId)) {
                System.out.println(" ");
                System.out.println("Подзадача с ID: " + uniqueId + " удалена!");
                subtasks.remove(subtask);
                return;
            }
        }
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getEpicsSubtasks(Integer uniqueId) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getEpicId(), uniqueId)) {
                epicsSubtasks.add(subtask);
            }
        }
        return epicsSubtasks;
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Integer addNewTask(Task task) {
        if (task == null) {
            System.out.println("Задачу невозможно сохранить");
            return null;
        }
        task.setUniqueId(generateId());
        tasks.add(task);
        return task.getUniqueId();
    }

    public void updateTask(Integer uniqueId, Task newTask) {
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (Objects.equals(task.getUniqueId(), uniqueId)) {
                    Collections.replaceAll(tasks, task, newTask);
                    System.out.println("Задача обновлена");
                    return;
                }
            }
        }
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Integer addNewEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Эпик невозможно сохранить");
            return null;
        }
        epic.setUniqueId(generateId());
        epics.add(epic);
        return epic.getUniqueId();
    }

    public void updateEpic(Integer uniqueId, Epic newEpic) {
        if (!epics.isEmpty()) {
            for (Epic epic : epics) {
                if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                    Collections.replaceAll(epics, epic, newEpic);
                    System.out.println("Эпик обновлен");
                    return;
                }
            }
        }
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Integer addNewSubtask(Subtask subtask, Epic epic) {
        int epicId = epics.indexOf(epic);
        Epic epicL = epics.get(epicId);
        subtask.setUniqueId(generateId());
        subtasks.add(subtask);
        updateStatus(subtask.getEpicId());
        epicL.addSubtaskIdToList(subtask.getUniqueId());
        return subtask.getUniqueId();
    }

    public void updateSubtask(Integer uniqueId, Subtask newSubtask) {
        ArrayList<Subtask> checkEpic = getEpicsSubtasks(uniqueId);
        if (checkEpic.isEmpty()) {
            System.out.println("Эпика к котрому вы хотите создать подзадачу нет");
            return;
        }
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (Objects.equals(subtask.getUniqueId(), uniqueId)) {
                    Collections.replaceAll(subtasks, subtask, newSubtask);
                    System.out.println("Подзадача обновлена");
                    updateStatus(subtask.getEpicId());
                    return;
                }
            }
        }
    }

    //Создание Id.
    private Integer generateId() {
        return id++;
    }

    //Управление статусами осуществляется по следующему правилу:
    //Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
    //По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    //Для эпиков:
    //если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    //если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    //во всех остальных случаях статус должен быть IN_PROGRESS.
    private void updateStatus(Integer uniqueId) {
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getUniqueId(), uniqueId)) {
                ArrayList<Subtask> epicSubtasks = getEpicsSubtasks(uniqueId);
                if (epicSubtasks.isEmpty()) {
                    epic.setStatus(Status.NEW);
                    return;
                }
                for (Subtask epicSubtask : epicSubtasks) {
                    if (epicSubtask.getStatus() == Status.NEW) {
                        epic.setStatus(Status.NEW);
                    } else if (epicSubtask.getStatus() == Status.IN_PROGRESS) {
                        epic.setStatus(Status.IN_PROGRESS);
                    } else if (epicSubtask.getStatus() == Status.DONE
                            && epicSubtask.getStatus() != Status.IN_PROGRESS
                            && epicSubtask.getStatus() != Status.NEW) {
                        epic.setStatus(Status.DONE);
                    }
                }
            }
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Epic> getEpics() {
        return epics;
    }

    public void setEpics(ArrayList<Epic> epics) {
        this.epics = epics;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
