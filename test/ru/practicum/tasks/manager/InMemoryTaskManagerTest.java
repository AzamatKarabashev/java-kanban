package ru.practicum.tasks.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.imp.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
        init();
    }

    @Test
    public void whenCallGenerateId_thenReturnIntegerPlusOne() {
        int id1 = manager.addNewTask(task1);
        assertEquals(0, id1);
        int id2 = manager.addNewEpic(epic1);
        assertEquals(1, id2);
    }
}