package ru.practicum.tasks.manager.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.api.TaskManagerTest;
import ru.practicum.tasks.manager.exception.ManagerSaveException;
import ru.practicum.tasks.manager.impl.FileBackedTasksManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.tasks.manager.impl.FileBackedTasksManager.restoreFromFile;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void setUp() {
        manager = new FileBackedTasksManager();
        init();
    }

    File file = new File("COMMA-COMMA.csv");

    @Test
    public void giveAllTypeOfTasks_whenCreateAndSaveNewCSVFile_thenRestoreFromFile() {
        FileBackedTasksManager testManager = new FileBackedTasksManager();
        testManager.addNewTask(task1);
        testManager.addNewEpic(epic1);
        testManager.addNewSubtask(subtask1, epic1);
        testManager.addNewSubtask(subtask2, epic1);

        testManager.getTaskById(task1.getId());
        testManager.getEpicById(epic1.getId());
        testManager.getSubtaskById(subtask1.getId());

        FileBackedTasksManager restoredManager = restoreFromFile(file);
        assertEquals(1, restoredManager.getTasks().size());
        assertEquals(1, restoredManager.getEpics().size());
        assertEquals(2, restoredManager.getSubtasks().size());
        assertEquals(epic1, restoredManager.getEpicById(epic1.getId()));
        assertEquals(subtask2, restoredManager.getSubtasks().get(1));
    }

    @Test
    public void giveWrongFilePath_whenTryToRestoreFromFile_thenThrowCustomException() {
        File wrongFile = new File("NotExist.csv");
        try {
            FileBackedTasksManager restoredManager = restoreFromFile(wrongFile);
        } catch (ManagerSaveException e) {
            assertEquals("NotExist.csv", e.getMessage());
        }
    }
}