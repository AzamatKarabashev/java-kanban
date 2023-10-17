package ru.practicum.tasks.manager.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.api.TaskManagerTest;
import ru.practicum.tasks.manager.impl.HttpTaskManager;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.tasks.model.Status.IN_PROGRESS;
import static ru.practicum.tasks.model.Status.NEW;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager();
        init();
    }

    @AfterEach
    public void shutDown() {
        kvServer.stop();
    }

    @Test
    public void testLoadFromHttpServer() throws IOException {
        HttpTaskManager manager = new HttpTaskManager();

        //создаем задачи
        Task testTask1 = new Task("Таска1", "Описание Таски1", NEW);
        int testIdTask = manager.addNewTask(testTask1);

        Epic testEpic1 = new Epic("Эпик1", "Описание Эпика1", NEW);
        int testIdEpic = manager.addNewEpic(testEpic1);

        Subtask testSubtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", IN_PROGRESS, testIdEpic);
        int testIdSubtask = manager.addNewSubtask(testSubtask1, testEpic1);

        //создаем историю
        manager.getTaskById(testIdTask);
        manager.getEpicById(testIdEpic);
        manager.getSubtaskById(testIdSubtask);

        //создаем менеджер с восстановленными объектами
        HttpTaskManager restoreManager = new HttpTaskManager();
        Task restoreTask1 = restoreManager.getTaskById(testIdTask);
        Epic restoreEpic1 = restoreManager.getEpicById(testIdEpic);
        Subtask restoreSubtask1 = restoreManager.getSubtaskById(testIdSubtask);

        //проверям восстановление задач
        assertEquals(testTask1, restoreTask1);
        assertEquals(testEpic1.getSubtaskIds().size(), restoreEpic1.getSubtaskIds().size());
        assertEquals(testSubtask1, restoreSubtask1);

        //проверяем восстановление истории
        assertEquals(manager.getInMemoryHistoryManager().getHistory()
                , restoreManager.getInMemoryHistoryManager().getHistory());

        //проверяемм восстановление листов задач
        assertEquals(manager.getTasks(), restoreManager.getTasks());
        assertEquals(manager.getEpics(), restoreManager.getEpics());
        assertEquals(manager.getSubtasks(), restoreManager.getSubtasks());
    }
}