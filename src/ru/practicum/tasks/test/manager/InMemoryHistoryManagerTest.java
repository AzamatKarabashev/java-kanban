package ru.practicum.tasks.test.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.HistoryManager;
import ru.practicum.tasks.manager.Managers;
import ru.practicum.tasks.model.task.Task;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.tasks.model.Status.NEW;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;

    Task task1 = new Task("T1", "D1", NEW);
    Task task2 = new Task("T2", "D2", NEW);
    Task task3 = new Task("T3", "D3", NEW);

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
    }

    @Test
    public void whenHistoryEmpty_thenReturnEmptyList() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void givenTask_whenHistoryFilledWithViewed_thenReturnFilledList() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.add(task3);
        historyManager.add(task2);
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void givenTask_whenRemoveFromHistory_thenRemoveEdgePositions() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertTrue(historyManager.getHistory().contains(task1));
        assertTrue(historyManager.getHistory().contains(task2));
        assertTrue(historyManager.getHistory().contains(task3));
        historyManager.remove(task1.getId());
        historyManager.remove(task2.getId());
        historyManager.remove(task3.getId());
        assertFalse(historyManager.getHistory().contains(task1));
        assertFalse(historyManager.getHistory().contains(task2));
        assertFalse(historyManager.getHistory().contains(task3));
        assertTrue(historyManager.getHistory().isEmpty());
    }
}