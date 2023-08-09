package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.util.List;

public interface TaskManager {

    void printHistory();

    void removeAllTasks();

    //Получение по идентификатору.
    Task getTaskById(Integer uniqueId);

    Epic getEpicById(Integer uniqueId);

    Subtask getSubtaskById(Integer uniqueId);

    //Удаление по идентификатору.
    void removeTask(Integer uniqueId);

    void removeEpic(Integer uniqueId);

    void removeSubtask(Integer uniqueId);

    //Получение списка всех подзадач определённого эпика.
    List<Subtask> getEpicsSubtasks(Integer uniqueId);

    //Создание. Сам объект должен передаваться в качестве параметра.
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Integer addNewTask(Task task);

    void updateTask(Task newTask);

    //Создание. Сам объект должен передаваться в качестве параметра.
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Integer addNewEpic(Epic epic);

    void updateEpic(Epic newEpic);

    //Создание. Сам объект должен передаваться в качестве параметра.
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Integer addNewSubtask(Subtask subtask, Epic epic);

    void updateSubtask(Subtask newSubtask);

    //Создание Id.

    Integer generateId();

    //Управление статусами осуществляется по следующему правилу:
    //Менеджер сам не выбирает статус для задачи. Информация о нём
    //приходит менеджеру вместе с информацией о самой задаче.
    //По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    //Для эпиков:
    //если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    //если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    //во всех остальных случаях статус должен быть IN_PROGRESS.

    void updateStatus(Integer uniqueId);

    //добавляем сюда методы get так как теперь используем интерфейс

    List<Task> getTasks();

    void setTasks(List<Task> tasks);

    List<Epic> getEpics();

    void setEpics(List<Epic> epics);

    List<Subtask> getSubtasks();

    void setSubtasks(List<Subtask> subtasks);

}
