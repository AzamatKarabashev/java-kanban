package ru.practicum.tasks.manager.imp;

import ru.practicum.tasks.manager.api.HistoryManager;
import ru.practicum.tasks.manager.api.TaskManager;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.tasks.manager.api.Managers.getDefaultHistory;
import static ru.practicum.tasks.model.Status.*;

public class InMemoryTaskManager implements TaskManager {

    protected Integer id = 0;

    protected List<Task> tasks = new ArrayList<>();
    protected List<Epic> epics = new ArrayList<>();
    protected List<Subtask> subtasks = new ArrayList<>();
    protected final HistoryManager inMemoryHistoryManager = getDefaultHistory();

    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.nullsLast((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        } else if (o1 == o2) {
            return 0;
        } else {
            return 1;
        }
    }));

    /**
     * Удаление всех задач/эпиков/подзадач
     * из соответствующих списков
     */
    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
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
            if (Objects.equals(epic, getEpicById(uniqueId))) {
                if (!epic.getSubtaskIds().isEmpty()) {
                    for (Integer subtaskId : epic.getSubtaskIds()) {
                        removeSubtaskById(subtaskId);
                    }
                }
                epics.remove(epic);
                inMemoryHistoryManager.remove(uniqueId);
                return;
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
            if (Objects.equals(subtask, getSubtaskById(uniqueId))) {
                subtasks.remove(subtask);
                inMemoryHistoryManager.remove(uniqueId);
                updateEpicStatus(subtask.getEpicId());
                calculateStartTimeForEpic(subtask.getEpicId());
                calculateDurationTimeForEpic(subtask.getEpicId());
                calculateEndTimeForEpic(subtask.getEpicId());
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
    public List<Subtask> getEpicSubtasksByEpicId(Integer uniqueId) {
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
            prioritizedTasks.add(task);
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
                    prioritizedTasks.remove(task);
                    prioritizedTasks.add(newTask);
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
            prioritizedTasks.add(epic);
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
                    prioritizedTasks.remove(epic);
                    prioritizedTasks.add(newEpic);
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
        if (epic != null && subtask != null) {
            subtask.setId(generateUniqueId());
            subtask.setEpicId(epic.getId());
            subtasks.add(subtask);
            epic.addSubtaskIdToList(subtask.getId());
            updateEpicStatus(subtask.getEpicId());
            prioritizedTasks.add(subtask);
            calculateStartTimeForEpic(subtask.getEpicId());
            calculateEndTimeForEpic(subtask.getEpicId());
            return subtask.getId();
        }
        return null;
    }

    /**
     * Принимает сабтаску с заранее известным нам uniqueId,
     * ищет в списке сабтаску с таким же id, заменяет старую на новую/переданную сабтаску
     * также, вызывает метод обновления статуса эпика
     */
    @Override
    public void updateSubtask(Subtask newSubtask) {
        List<Subtask> checkEpic = getEpicSubtasksByEpicId(newSubtask.getEpicId());
        if (checkEpic.isEmpty()) {
            return;
        }
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (Objects.equals(subtask.getId(), newSubtask.getId())) {
                    Collections.replaceAll(subtasks, subtask, newSubtask);
                    updateEpicStatus(subtask.getEpicId());
                    prioritizedTasks.remove(subtask);
                    prioritizedTasks.add(newSubtask);
                    calculateStartTimeForEpic(subtask.getEpicId());
                    calculateDurationTimeForEpic(subtask.getEpicId());
                    calculateEndTimeForEpic(subtask.getEpicId());
                    return;
                }
            }
        }
    }

    /**
     * Наш любимый генератор uniqueId
     */

    //Создание Id.
    protected Integer generateUniqueId() {
        return id++;
    }

    /**
     * Обновление статуса эпика в зависимости от статуса привязанных к нему сабтасок
     */
    @Override
    public void updateEpicStatus(Integer uniqueId) {
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), uniqueId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicId(uniqueId);
                if (epicSubtasks.isEmpty()) {
                    epic.setStatus(NEW);
                    return;
                }
                for (Subtask epicSubtask : epicSubtasks) {
                    if (epicSubtask.getStatus() != NEW && epicSubtask.getStatus() != DONE
                            && epicSubtask.getStatus() == IN_PROGRESS) {
                        epic.setStatus(IN_PROGRESS);
                    } else if (epicSubtask.getStatus() != NEW && epicSubtask.getStatus() == DONE
                            && epicSubtask.getStatus() != IN_PROGRESS) {
                        epic.setStatus(DONE);
                    } else if (epicSubtask.getStatus() != NEW && epicSubtask.getStatus() == DONE
                            && epicSubtask.getStatus() == IN_PROGRESS) {
                        epic.setStatus(IN_PROGRESS);
                    } else {
                        epic.setStatus(NEW);
                    }
                }
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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

    @Override
    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public void calculateStartTimeForEpic(Integer epicId) {
        List<LocalDateTime> test = new ArrayList<>();
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicId(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getStartTime() != null) {
                        test.add(subtask.getStartTime());
                        LocalDateTime min = Collections.min(test);
                        epic.setStartTime(min);
                        calculateDurationTimeForEpic(epic.getId());
                    }
                }
            }
        }
    }

    @Override
    public void calculateDurationTimeForEpic(Integer epicId) {
        Duration duration = Duration.ZERO;
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicId(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getDuration() == null) {
                        epic.setDuration(duration);
                        return;
                    }
                    duration = duration.plus(subtask.getDuration());
                    epic.setDuration(duration);
                }
            }
        }
    }

    @Override
    public void calculateEndTimeForEpic(Integer epicId) {
        List<LocalDateTime> test = new ArrayList<>();
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getEpicSubtasksByEpicId(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getEndTime() != null) {
                        test.add(subtask.getEndTime());
                        LocalDateTime max = Collections.max(test);
                        epic.setEndTime(max);
                    }
                }
            }
        }
    }

    @Override
    public boolean isIntersection(Task task) {
        if (task == null) {
            return false;
        }
        if (task.getStartTime() == null) {
            return false;
        }
        if (prioritizedTasks.isEmpty()) {
            return true;
        }
        for (Task prioritized : getPrioritizedTasks()) {
            if (prioritized.getStartTime() == null) {
                continue;
            }
            if (task.getStartTime().isAfter(prioritized.getStartTime())
                    || task.getEndTime().isBefore(prioritized.getEndTime())) {
                return false;
            }
        }
        return true;
    }
}

