package ru.practicum.tasks.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.api.TaskManager;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.tasks.model.Status.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;

    protected void init() {
        task1 = new Task("Task1", "DescriptionTask1", NEW);
        task2 = new Task("Task2", "DescriptionTask2", NEW);
        epic1 = new Epic("Epic1", "DescriptionEpic1", NEW);
        epic2 = new Epic("Epic2", "DescriptionEpic2", NEW);
        subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", NEW, epic1.getId());
        subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", NEW, epic1.getId());
        task1.setId(0);
        task2.setId(1);
        epic1.setId(2);
        epic2.setId(3);
        subtask1.setId(4);
        subtask2.setId(5);
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
        assertEquals(emptyList(), manager.getTasks());
        assertEquals(emptyList(), manager.getEpics());
        assertEquals(emptyList(), manager.getSubtasks());
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
        assertEquals(emptyList(), manager.getTasks());
        assertNull(manager.getTaskById(task1.getId()));
    }

    @Test
    public void givenIdInParam_whenCallMethodRemoveEpicById_thenRemoveEpic() {
        manager.addNewEpic(epic1);
        assertNotNull(manager.getEpicById(epic1.getId()));
        manager.removeEpicById(epic1.getId());
        assertEquals(emptyList(), manager.getEpics());
        assertNull(manager.getEpicById(epic1.getId()));
    }

    @Test
    public void givenIdInParam_whenCallMethodRemoveSubtaskById_thenRemoveSubtask() {
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1, epic1);
        assertNotNull(manager.getEpicById(epic1.getId()));
        assertNotNull(manager.getSubtaskById(subtask1.getId()));
        manager.removeSubtaskById(subtask1.getId());
        assertEquals(emptyList(), manager.getSubtasks());
        assertNull(manager.getSubtaskById(subtask1.getId()));
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
        assertEquals(task1, manager.getPrioritizedTasks().get(0));
        Task test = new Task("Test", "TestDescription", NEW);
        test.setId(task1.getId());
        manager.updateTask(test);
        for (Task task : manager.getTasks()) {
            assertEquals(test, task);
        }
        assertEquals(test, manager.getPrioritizedTasks().get(0));
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
        manager.addNewTask(task1);
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1, epic1);
        assertEquals(subtask1, manager.getSubtasks().get(0));
        assertEquals(3, manager.getPrioritizedTasks().size());
        Subtask test = new Subtask("Test", "TestDescription", NEW, epic1.getId());
        test.setId(subtask1.getId());
        manager.updateSubtask(test);
        for (Subtask subtask : manager.getSubtasks()) {
            assertEquals(test, subtask);
        }
        assertTrue(manager.getPrioritizedTasks().contains(test));
    }

    @Test
    public void giveId_whenUpdateStatus_thenChangeEpicStatus() {
        Epic testEpic1 = new Epic("Test1", "Description", NEW);
        manager.addNewEpic(testEpic1);
        assertEquals(NEW, testEpic1.getStatus());
        Subtask testSubtask1 = new Subtask("Test2", "Description", NEW, testEpic1.getId());
        manager.addNewSubtask(testSubtask1, testEpic1);
        assertEquals(NEW, testEpic1.getStatus());
        Subtask testSubtask2 = new Subtask("Test4", "Description", IN_PROGRESS, testEpic1.getId());
        manager.addNewSubtask(testSubtask2, testEpic1);
        assertEquals(IN_PROGRESS, testEpic1.getStatus());
        testSubtask1.setStatus(DONE);
        testSubtask2.setStatus(DONE);
        manager.updateEpicStatus(testEpic1.getId());
        assertEquals(DONE, testEpic1.getStatus());
    }

    @Test
    public void whenGetTasks_thenReturnListFillOfTasks() {
        assertEquals(emptyList(), manager.getTasks());
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void whenGetEpics_thenReturnListFillOfEpics() {
        assertEquals(emptyList(), manager.getEpics());
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        assertEquals(2, manager.getEpics().size());
    }

    @Test
    public void whenGetSubtasks_thenReturnListFillOfSubtasks() {
        manager.addNewEpic(epic1);
        assertEquals(emptyList(), manager.getSubtasks());
        manager.addNewSubtask(subtask1, epic1);
        manager.addNewSubtask(subtask2, epic1);
        assertEquals(2, manager.getSubtasks().size());
    }

    @Test
    public void whenGetPrioritizedTasks_thenReturnTreeSetFillOfTasks() {
        task1.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 4, 21, 59));
        task2.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 4, 22, 30));
        epic1.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 3, 20, 10));
        assertTrue(manager.getPrioritizedTasks().isEmpty());
        manager.addNewTask(task1);
        assertEquals(1, manager.getPrioritizedTasks().size());
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        assertEquals(3, manager.getPrioritizedTasks().size());
        assertEquals(epic1, manager.getPrioritizedTasks().get(0));
    }

    @Test
    public void givenStartTimeAndDuration_whenAddSubtask_thenCalculateTimeFieldsOfEpic() {
        manager.addNewEpic(epic1);
        subtask1.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 5, 10, 1));
        subtask1.setDuration(Duration.ofMinutes(59));
        manager.addNewSubtask(subtask1, epic1);
        assertEquals(LocalDateTime.of(2023, Month.OCTOBER, 5, 10, 1), epic1.getStartTime());
        assertEquals(Duration.ofMinutes(59), epic1.getDuration());

        subtask2.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 5, 11, 0));
        subtask2.setDuration(Duration.ofMinutes(30));
        manager.addNewSubtask(subtask2, epic1);
        assertEquals(LocalDateTime.of(2023, Month.OCTOBER, 5, 10, 1), epic1.getStartTime());
        assertEquals(Duration.ofMinutes(89), epic1.getDuration());
        assertFalse(manager.isIntersection(subtask1));

        task1.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 5, 10, 20));
        task1.setDuration(Duration.ofMinutes(10));
        manager.addNewTask(task1);
        assertFalse(manager.isIntersection(task1));

        manager.calculateEndTimeForEpic(epic1.getId());
        assertEquals(LocalDateTime.of(2023, Month.OCTOBER, 5, 11, 30), epic1.getEndTime());
    }
}
