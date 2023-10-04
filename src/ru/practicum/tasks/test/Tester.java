package ru.practicum.tasks.test;

import ru.practicum.tasks.manager.TaskManager;
import ru.practicum.tasks.model.task.Epic;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.task.Subtask;
import ru.practicum.tasks.model.task.Task;

import java.util.List;

import static ru.practicum.tasks.manager.Managers.getDefault;

public class Tester {

    public void taskPrinter(Task task, Integer taskId) {
        System.out.println("Название задачи: " + task.getName() + ".\n"
                + "Описание задачи: " + task.getDescription() + ".\n"
                + "Статус задачи: " + task.getStatus() + ".\n"
                + "Уникальный ID задачи: " + taskId + ".\n");
    }

    public void epicPrinter(Epic epic, Integer epicId) {
        System.out.println("Название эпика: " + epic.getName() + ".\n"
                + "Описание эпика: " + epic.getDescription() + ".\n"
                + "Статус эпика: " + epic.getStatus() + ".\n"
                + "Уникальный ID эпика: " + epicId + ".\n");
    }

    public void subtaskPrinter(Subtask subtask, Integer subtaskId) {
        System.out.println("Название подзадачи: " + subtask.getName() + ".\n"
                + "Описание подзадачи: " + subtask.getDescription() + ".\n"
                + "Статус подзадачи: " + subtask.getStatus() + ".\n"
                + "ID Эпика в рамках которого создана подзадача: " + subtask.getEpicId() + ".\n"
                + "Уникальный ID подзадачи: " + subtaskId + ".\n");
    }

    public void test() {

        TaskManager inMemoryTaskManager = getDefault();

        System.out.println("Тесты: создаем и печатаем задачи/эпики/подзадачи.\n");

        Task task1 = new Task("Убраться в гараже", "Уровень невозможно, добро пожаловать", Status.NEW);
        int task1Id = inMemoryTaskManager.addNewTask(task1);
        taskPrinter(task1, task1Id);

        Task task2 = new Task("Поздравить бабушку с днем рождения", "Забыл = проклят", Status.NEW);
        int task2Id = inMemoryTaskManager.addNewTask(task2);
        taskPrinter(task2, task2Id);

        Epic epic1 = new Epic("Привести сон в норму", "Нормализация сна", Status.NEW);
        int epic1Id = inMemoryTaskManager.addNewEpic(epic1);
        epicPrinter(epic1, epic1Id);

        Subtask subtask1 = new Subtask("Купить будильник", "Важная деталь", Status.DONE, epic1Id);
        int subtask1Id = inMemoryTaskManager.addNewSubtask(subtask1, epic1);
        subtaskPrinter(subtask1, subtask1Id);

        Subtask subtask2 = new Subtask("Запрет на соцсети после 22 часов",
                "Действие осуществляется после покупки будильника", Status.DONE, epic1Id);
        int subtask2Id = inMemoryTaskManager.addNewSubtask(subtask2, epic1);
        subtaskPrinter(subtask2, subtask2Id);

        Epic epic2 = new Epic("Обрадовать жену", "Хочешь похвалы?", Status.NEW);
        int epic2Id = inMemoryTaskManager.addNewEpic(epic2);
        epicPrinter(epic2, epic2Id);

        Subtask subtask3 = new Subtask("Починить кран", "Тот день, когда белоручка взял инструменты",
                Status.IN_PROGRESS, epic2Id);
        int subtask3Id = inMemoryTaskManager.addNewSubtask(subtask3, epic2);
        subtaskPrinter(subtask3, subtask3Id);

        System.out.println("\nТесты: печатаем список всех задач для проверки.\n");
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
            System.out.println(" ");
        }

        System.out.println("\nТесты: печатаем список всех эпиков для проверки.\n");
        for (Epic epics : inMemoryTaskManager.getEpics()) {
            System.out.println(epics);
            System.out.println(" ");
        }

        System.out.println("\nТесты: печатаем список всех подзадач для проверки.\n");
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
            System.out.println(" ");
        }

        System.out.println("\nТесты: печатаем списки подзадач привязанных к эпику.\n");
        List<Subtask> epic1Subtasks = inMemoryTaskManager.getEpicSubtasksByEpicId(epic1Id);
        for (Subtask epic1Subtask : epic1Subtasks) {
            System.out.println(epic1Subtask);
            System.out.println(" ");
        }

        System.out.println("\nТесты: печатаем эпики для проверки обновления статуса эпика.\n");
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
            System.out.println(" ");
        }

        System.out.println("\nТесты: обновляем задачки.\n");
        inMemoryTaskManager.updateTask(task2);
        System.out.println(" ");

        System.out.println("\nТесты: снова печатаем список всех задач для проверки.\n");
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
            System.out.println(" ");
        }

        System.out.println("\nТесты: печатаем айдишники сабтасок у определенного эпика.\n");
        System.out.println("У эпика с ID: " + epic1Id + ".");
        System.out.println(" ");
        for (Integer integer : epic1.getSubtaskIds()) {
            System.out.println("Сабтаска с ID: " + integer);
            System.out.println(" ");
        }

        System.out.println("\nТесты: проверка работы методов удаления.");
        inMemoryTaskManager.removeTaskById(task2Id);
        System.out.println(" ");

        System.out.println("\nТесты: проверка удаления эпика, после чего должны удалится его сабтаски.");
        inMemoryTaskManager.removeEpicById(epic1Id);
        System.out.println(" ");
        System.out.println("После удаления эпика в списке сабтасок осталось: ");
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println("Сабтаска с ID: " + subtask.getId() + "\n");
        }

        System.out.println("\nТесты: проверка обновления статуса эпика при удалении сабтасок.\n");
        System.out.println("Для теста взяли эпик с ID: " + epic2.getId()
                + ". его статус: " + epic2.getStatus() + "\n");
        System.out.println("Для начала мы проверим существование нужных нам для теста сабтасок.\n");
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        List<Subtask> checkWithDebug = inMemoryTaskManager.getSubtasks();
        inMemoryTaskManager.removeSubtaskById(subtask3Id);
        System.out.println("\nМы удалили сабтаски первого эпика с ID: " + epic2.getId()
                + ". теперь его статус: " + epic2.getStatus() + "\n");
        System.out.println();

        /*
        Тестирование работы программы
        После написания менеджера истории проверьте его работу:
        создайте две задачи, эпик с тремя подзадачами и эпик без подзадач;
        запросите созданные задачи несколько раз в разном порядке;
        после каждого запроса выведите историю и убедитесь, что в ней нет повторов;
        удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться;
        удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        Интересного вам программирования!
         */

        System.out.println("\nТесты: тестируем работу менеджера истории просмотра!\n");
        Task taskForTestHistoryManager1 = new Task("Первая таска для теста менеджера истории",
                "Описание первой тестовой таски", Status.NEW);
        int taskForTestHistoryManager1Id = inMemoryTaskManager.addNewTask(taskForTestHistoryManager1);
        taskPrinter(taskForTestHistoryManager1, taskForTestHistoryManager1Id);

        Task taskForTestHistoryManager2 = new Task("Вторая таска для теста менеджера истории",
                "Описание второй тестовой таски", Status.NEW);
        int taskForTestHistoryManager2Id = inMemoryTaskManager.addNewTask(taskForTestHistoryManager2);
        taskPrinter(taskForTestHistoryManager2, taskForTestHistoryManager2Id);

        Epic epicForTestHistoryManager1 = new Epic("Первый эпик для теста менеджера истории с тремя сабтасками",
                "Описание первого тестового эпика", Status.NEW);
        int epicForTestHistoryManager1Id = inMemoryTaskManager.addNewEpic(epicForTestHistoryManager1);
        epicPrinter(epicForTestHistoryManager1, epicForTestHistoryManager1Id);

        Subtask firstTestSubtaskOfTestEpic = new Subtask("Первая сабтаска эпика", "Описание",
                Status.DONE, epicForTestHistoryManager1Id);
        int firstTestSubtaskOfTestEpicId = inMemoryTaskManager.addNewSubtask(firstTestSubtaskOfTestEpic,
                epicForTestHistoryManager1);
        subtaskPrinter(firstTestSubtaskOfTestEpic, firstTestSubtaskOfTestEpicId);

        Subtask secondTestSubtaskOfTestEpic = new Subtask("Вторая сабтаска эпика", "Описание",
                Status.DONE, epicForTestHistoryManager1Id);
        int secondTestSubtaskOfTestEpicId = inMemoryTaskManager.addNewSubtask(secondTestSubtaskOfTestEpic,
                epicForTestHistoryManager1);
        subtaskPrinter(secondTestSubtaskOfTestEpic, secondTestSubtaskOfTestEpicId);

        Subtask thirdTestSubtaskOfTestEpic = new Subtask("Третья сабтаска эпика", "Описание",
                Status.DONE, epicForTestHistoryManager1Id);
        int thirdTestSubtaskOfTestEpicId = inMemoryTaskManager.addNewSubtask(thirdTestSubtaskOfTestEpic,
                epicForTestHistoryManager1);
        subtaskPrinter(thirdTestSubtaskOfTestEpic, thirdTestSubtaskOfTestEpicId);

        Epic epicForTestHistoryManager2 = new Epic("Второй эпик для теста менеджера истории без сабтасок",
                "Описание первого тестового эпика", Status.NEW);
        int epicForTestHistoryManager2Id = inMemoryTaskManager.addNewEpic(epicForTestHistoryManager2);
        epicPrinter(epicForTestHistoryManager2, epicForTestHistoryManager2Id);


    }
}
