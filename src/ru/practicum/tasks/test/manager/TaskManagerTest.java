package ru.practicum.tasks.test.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.Managers;
import ru.practicum.tasks.manager.TaskManager;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.tasks.model.Status.*;

public abstract class TaskManagerTest {

    TaskManager manager;
    Task task1 = new Task("Task1", "DescriptionTask1", NEW);
    Task task2 = new Task("Task2", "DescriptionTask2", IN_PROGRESS);
    Epic epic1 = new Epic("Epic1", "DescriptionEpic1", NEW);
    Epic epic2 = new Epic("Epic2", "DescriptionEpic2", NEW);
    Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", IN_PROGRESS, epic1.getId());
    Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", DONE, epic1.getId());

    @BeforeEach
    public void createClassExemplar() {
        manager = Managers.getDefaultInMemory();
    }

    @Test
    public void givenCallRemoveAllTasksMethod_whenRemoveAllTasks_thenAllListsAreEmpty() {
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewSubtask(subtask1, epic1);
        manager.addNewSubtask(subtask2, epic2);
        assertFalse(manager.getTasks().isEmpty());
        assertFalse(manager.getEpics().isEmpty());
        assertFalse(manager.getSubtasks().isEmpty());
        assertFalse(manager.getPrioritizedTasks().isEmpty());
        manager.removeAllTasks();
        assertEquals(Collections.emptyList(), manager.getTasks());
        assertEquals(Collections.emptyList(), manager.getEpics());
        assertEquals(Collections.emptyList(), manager.getSubtasks());
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void givenIdInParam_whenCallMethodGetTaskById_thenReturnTask() {
        manager.addNewTask(task1);
        manager.getTaskById(task1.getId());
        assertFalse(manager.getPrioritizedTasks().isEmpty());
        assertEquals(task1, manager.getTaskById(task1.getId()));
    }

    @Test
    public void givenIdInParam_whenCallMethodGetEpicById_thenReturnEpic() {
        manager.addNewEpic(epic1);
        manager.getEpicById(epic1.getId());
        assertFalse(manager.getPrioritizedTasks().isEmpty());
        assertEquals(epic1, manager.getEpicById(epic1.getId()));
    }

    @Test
    public void givenIdInParam_whenCallMethodGetSubtaskById_thenReturnSubtask() {
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1, epic1);
        assertFalse(manager.getPrioritizedTasks().isEmpty());
        assertEquals(subtask1, manager.getSubtaskById(subtask1.getId()));
    }

    @Test
    public void givenIdInParam_whenCallMethodRemoveTaskById_thenRemoveTask() {
        manager.addNewTask(task1);
        assertNotNull(manager.getTaskById(task1.getId()));
        manager.removeTaskById(task1.getId());
        assertEquals(Collections.emptyList(), manager.getTasks());
        assertNull(manager.getTaskById(task1.getId()));
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void givenIdInParam_whenCallMethodRemoveEpicById_thenRemoveEpic() {
        manager.addNewEpic(epic1);
        assertNotNull(manager.getEpicById(epic1.getId()));
        manager.removeEpicById(epic1.getId());
        assertEquals(Collections.emptyList(), manager.getEpics());
        assertNull(manager.getEpicById(epic1.getId()));
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void givenIdInParam_whenCallMethodRemoveSubtaskById_thenRemoveSubtask() {
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1, epic1);
        assertNotNull(manager.getEpicById(epic1.getId()));
        assertNotNull(manager.getSubtaskById(subtask1.getId()));
        manager.removeSubtaskById(subtask1.getId());
        assertFalse(manager.getEpics().isEmpty());
        assertEquals(Collections.emptyList(), manager.getSubtasks());
        assertNull(manager.getSubtaskById(subtask1.getId()));
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void givenIdInParam_whenCallMethodGetEpicSubtasksByEpicId_thenReturnListFillOfSubtasks() {
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1, epic1);
        manager.addNewSubtask(subtask2, epic1);
        assertNotNull(manager.getEpicById(epic1.getId()));
        assertNotNull(manager.getSubtaskById(subtask1.getId()));
        assertNotNull(manager.getSubtaskById(subtask1.getId()));
        assertFalse(manager.getEpicSubtasksByEpicId(epic1.getId()).isEmpty());
    }

    @Test
    public void whenCallMethodAddNewTask_thenReturnIdAndAddTask() {
        int id = manager.addNewTask(task1);
        assertEquals(0, id);
        assertEquals(1, manager.getTasks().size());
    }

    @Test
    public void whenCallMethodAddNewEpic_thenReturnIdAndAddEpic() {
        int id = manager.addNewEpic(epic1);
        assertEquals(0, id);
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    public void whenCallMethodAddNewSubtask_thenReturnIdAndAddSubtask() {
        manager.addNewEpic(epic1);
        int id = manager.addNewSubtask(subtask1, epic1);
        assertEquals(1, id);
        assertEquals(1, manager.getSubtasks().size());
    }

    @Test
    public void giveNewTaskInParam_whenCallMethodUpdateTask_thenReplaceOldTaskWithNewTask() {
        manager.addNewTask(task1);
        for (Task task : manager.getTasks()) {
            assertEquals(task1, task);
        }
        for (Task prioritizedTask : manager.getPrioritizedTasks()) {
            assertEquals(task1, prioritizedTask);
        }
        Task test = new Task("Test", "TestDescription", NEW);
        test.setId(task1.getId());
        manager.updateTask(test);
        for (Task task : manager.getTasks()) {
            assertEquals(test, task);
        }
        for (Task prioritizedTask : manager.getPrioritizedTasks()) {
            assertEquals(test, prioritizedTask);
        }
    }

    @Test
    public void giveNewEpicInParam_whenCallMethodUpdateEpic_thenReplaceOldEpicWithNewEpic() {
        manager.addNewEpic(epic1);
        for (Epic epic : manager.getEpics()) {
            assertEquals(epic1, epic);
        }
        for (Task prioritizedTask : manager.getPrioritizedTasks()) {
            assertEquals(epic1, prioritizedTask);
        }
        Epic test = new Epic("Test", "TestDescription", NEW);
        test.setId(epic1.getId());
        manager.updateEpic(test);
        for (Epic epic : manager.getEpics()) {
            assertEquals(test, epic);
        }
        for (Task prioritizedTask : manager.getPrioritizedTasks()) {
            assertEquals(test, prioritizedTask);
        }
    }

    @Test
    public void giveNewSubtaskInParam_whenCallMethodUpdateSubtask_thenReplaceOldSubtaskWithNewSubtask() {
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1, epic1);
        for (Subtask subtask : manager.getSubtasks()) {
            assertEquals(subtask1, subtask);
        }
        assertTrue(manager.getPrioritizedTasks().contains(subtask1));
        Subtask test = new Subtask("Test", "TestDescription", NEW, epic1.getId());
        test.setId(subtask1.getId());
        manager.updateSubtask(test);
        for (Subtask subtask : manager.getSubtasks()) {
            assertEquals(test, subtask);
        }
        assertTrue(manager.getPrioritizedTasks().contains(test));
    }

    @Test
    public void whenCallGenerateId_thenReturnIntegerPlusOne() {
        int id1 = manager.addNewTask(task1);
        assertEquals(0, id1);
        int id2 = manager.addNewEpic(epic1);
        assertEquals(1, id2);
    }

    @Test
    public void giveId_whenUpdateStatus_thenChangeEpicStatus() {
        Epic testEpic1 = new Epic("Test1", "Description", NEW);
        manager.addNewEpic(testEpic1);
        assertEquals(NEW, testEpic1.getStatus());
        Subtask testSubtask1 = new Subtask("Test2", "Description", NEW, testEpic1.getId());
        Subtask testSubtask2 = new Subtask("Test4", "Description", IN_PROGRESS, testEpic1.getId());
        manager.addNewSubtask(testSubtask1, testEpic1);
        assertEquals(NEW, testEpic1.getStatus());
        manager.addNewSubtask(testSubtask2, testEpic1);
        assertEquals(IN_PROGRESS, testEpic1.getStatus());
        testSubtask1.setStatus(DONE);
        testSubtask2.setStatus(DONE);
        manager.updateStatus(testEpic1.getId());
        assertEquals(DONE, testEpic1.getStatus());
    }




}
