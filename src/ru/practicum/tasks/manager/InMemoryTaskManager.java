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
    protected final HistoryManager inMemoryHistoryManager = getDefaultHistory();

    /**
     * Метод печатает историю просмотра задач из
     * InMemoryHistoryManager
     */
    @Override
    public void printHistory() {
        for (Task task : inMemoryHistoryManager.getHistory()) {
            System.out.println(task);
        }
    }

    /**
     * Удаление всех задач/эпиков/подзадач
     * из соответствующих списков
     */
    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    /**
     * Получение таски по ID;
     * метод пробегается по всем таскам,
     * ищет таску с переданным в метод параметром uniqueId
     * в случае успеха, возвращает таску с переданным uniqueId
     * Вернет null в случае, если таски с указанным uniqueId нет
     */
    @Override
    public Task getTaskById(Integer uniqueId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getId(), uniqueId)) {
                inMemoryHistoryManager.add(task);
                return task;
            }
        }
        return null;
    }

    /**
     * Получение эпика по ID;
     * метод пробегается по всем эпикам,
     * ищет эпик с переданным в метод параметром uniqueId
     * в случае успеха, возвращает эпик с переданным uniqueId
     * Вернет null в случае, если эпика с указанным uniqueId нет
     */
    @Override
    public Epic getEpicById(Integer uniqueId) {
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), uniqueId)) {
                inMemoryHistoryManager.add(epic);
                return epic;
            }
        }
        return null;
    }

    /**
     * Получение сабтаски по ID;
     * метод пробегается по всем сабтаскам,
     * ищет сабтаску с переданным в метод параметром uniqueId
     * в случае успеха, возвращает сабтаску с переданным uniqueId
     * Вернет null в случае, если сабтаски с указанным uniqueId нет
     */
    @Override
    public Subtask getSubtaskById(Integer uniqueId) {
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getId(), uniqueId)) {
                inMemoryHistoryManager.add(subtask);
                return subtask;
            }
        }
        return null;
    }

    /**
     * Удаление таски по переданнму uniqueId
     * метод пробегается по списку тасок, находит и удаляет таску с переданным uniqueId
     * Upd: ТЗ-5
     * Также, метод удаляет из истории просмотра указанную таску
     */
    @Override
    public void removeTaskById(Integer uniqueId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getId(), uniqueId)) {
                tasks.remove(task);
                inMemoryHistoryManager.remove(uniqueId);
                return;
            }
        }
    }

    /**
     * Удаление эпика по переданному uniqueId
     * метод пробегается по списку эпиков, находит и удаляет эпик с переданным uniqueId
     * Также, метод получает список id-шников сабтасок привязанных к эпику,
     * (если таковые имеются) вызывает удаление сабтасок.
     * Upd: ТЗ-5
     * Вместе с этим, удаляет из истории просмотров вышеуказанный эпик и его сабтаски, соответственно
     */
    @Override
    public void removeEpicById(Integer uniqueId) {
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), uniqueId)) {
                for (Integer integer : epic.getSubtaskIds()) {
                    removeSubtaskById(integer);
                }
                epics.remove(epic);
                inMemoryHistoryManager.remove(uniqueId);
            }
        }
    }

    /**
     * Удаление сабтаски по переданному uniqueId
     * Upd: ТЗ-5
     * также, указанный метод удалит сабтаску из истории просмотра задач
     */
    @Override
    public void removeSubtaskById(Integer uniqueId) {
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getId(), uniqueId)) {
                subtasks.remove(subtask);
                inMemoryHistoryManager.remove(uniqueId);
                updateStatus(subtask.getEpicId());
                return;
            }
        }
    }

    /**
     * Поиск сабтасок привязанных к эпику по переданному параметру uniqueId
     * возвращает List заполненный сабтасками определенного эпика,
     * в случае если у эпика нет привязанных сабтасок, вернется пустой лист
     */
    @Override
    public List<Subtask> getEpicSubtasks(Integer uniqueId) {
        List<Subtask> epicsSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (Objects.equals(subtask.getEpicId(), uniqueId)) {
                epicsSubtasks.add(subtask);
            }
        }
        return epicsSubtasks;
    }

    /**
     * Принимает таску, сохраняет ее в список тасок и возвращает uniqueId, переданной таски
     */
    @Override
    public Integer addNewTask(Task task) {
        if (task != null) {
            task.setId(generateUniqueId());
            tasks.add(task);
            return task.getId();
        }
        return null;
    }

    /**
     * Принимает таску с заранее известным нам uniqueId,
     * ищет в списке таску с таким же id, заменяет старую на новую/переданную таску
     */
    @Override
    public void updateTask(Task newTask) {
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (Objects.equals(task.getId(), newTask.getId())) {
                    Collections.replaceAll(tasks, task, newTask);
                    return;
                }
            }
        }
    }

    /**
     * Принимает эпик, сохраняет его в список эпиков и возвращает uniqueId, переданного эпика
     */
    @Override
    public Integer addNewEpic(Epic epic) {
        if (epic != null) {
            epic.setId(generateUniqueId());
            epics.add(epic);
            return epic.getId();
        }
        return null;
    }

    /**
     * Принимает эпик с заранее известным нам uniqueId,
     * ищет в списке эпик с таким же id, заменяет старый на новый/переданный эпик
     */
    @Override
    public void updateEpic(Epic newEpic) {
        if (!epics.isEmpty()) {
            for (Epic epic : epics) {
                if (Objects.equals(epic.getId(), newEpic.getId())) {
                    Collections.replaceAll(epics, epic, newEpic);
                    return;
                }
            }
        }
    }

    /**
     * Принимает сабтаску и эпик в рамках которого создана
     * также, вызывает метод обновления статуса эпика
     * возвращает uniqueId сабтаски
     */
    @Override
    public Integer addNewSubtask(Subtask subtask, Epic epic) {
        int epicId = epics.indexOf(epic);
        Epic epicL = epics.get(epicId);
        subtask.setId(generateUniqueId());
        subtasks.add(subtask);
        epicL.addSubtaskIdToList(subtask.getId());
        updateStatus(subtask.getEpicId());
        return subtask.getId();
    }

    /**
     * Принимает сабтаску с заранее известным нам uniqueId,
     * ищет в списке сабтаску с таким же id, заменяет старую на новую/переданную сабтаску
     * также, вызывает метод обновления статуса эпика
     */
    @Override
    public void updateSubtask(Subtask newSubtask) {
        List<Subtask> checkEpic = getEpicSubtasks(newSubtask.getEpicId());
        if (checkEpic.isEmpty()) {
            return;
        }
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (Objects.equals(subtask.getId(), newSubtask.getId())) {
                    Collections.replaceAll(subtasks, subtask, newSubtask);
                    updateStatus(subtask.getEpicId());
                    return;
                }
            }
        }
    }

    /**
     * Наш любимый генератор uniqueId
     */
    @Override
    //Создание Id.
    public Integer generateUniqueId() {
        return id++;
    }

    /**
     * Обновление статуса эпика в зависимости от статуса привязанных к нему сабтасок
     */
    @Override
    public void updateStatus(Integer uniqueId) {
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), uniqueId)) {
                List<Subtask> epicSubtasks = getEpicSubtasks(uniqueId);
                if (epicSubtasks.isEmpty()) {
                    epic.setStatus(Status.NEW);
                    return;
                }
                for (Subtask epicSubtask : epicSubtasks) {
                    if (epicSubtask.getStatus() == Status.IN_PROGRESS && epicSubtask.getStatus() != Status.NEW
                            && epicSubtask.getStatus() != Status.DONE) {
                        epic.setStatus(Status.IN_PROGRESS);
                    } else if (epicSubtask.getStatus() != Status.DONE && epicSubtask.getStatus() != Status.IN_PROGRESS
                            && epicSubtask.getStatus() == Status.NEW) {
                        epic.setStatus(Status.NEW);
                    } else {
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
