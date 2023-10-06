package ru.practicum.tasks.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.tasks.manager.FileBackedTasksManager.restoreFromFile;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void setUp() {
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
}