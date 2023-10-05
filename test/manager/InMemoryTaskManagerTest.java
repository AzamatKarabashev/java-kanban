package manager;

import org.junit.jupiter.api.Test;
import ru.practicum.tasks.manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    public void whenCallGenerateId_thenReturnIntegerPlusOne() {
        int id1 = manager.addNewTask(task1);
        assertEquals(0, id1);
        int id2 = manager.addNewEpic(epic1);
        assertEquals(1, id2);
    }
}