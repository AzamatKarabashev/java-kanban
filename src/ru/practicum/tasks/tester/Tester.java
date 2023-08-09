package ru.practicum.tasks.tester;

import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.manager.InMemoryTaskManager;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Tester {

    public void taskPrinter(Task task, Integer taskId) {
        System.out.println("Название задачи: " + task.getTaskName() + ".\n"
                + "Описание задачи: " + task.getTaskDesc() + ".\n"
                + "Статус задачи: " + task.getStatus() + ".\n"
                + "Уникальный ID задачи: " + taskId + ".\n");
    }

    public void epicPrinter(Epic epic, Integer epicId) {
        System.out.println("Название эпика: " + epic.getTaskName() + ".\n"
                + "Описание эпика: " + epic.getTaskDesc() + ".\n"
                + "Статус эпика: " + epic.getStatus() + ".\n"
                + "Уникальный ID эпика: " + epicId + ".\n");
    }

    public void subtaskPrinter(Subtask subtask, Integer subtaskId) {
        System.out.println("Название подзадачи: " + subtask.getTaskName() + ".\n"
                + "Описание подзадачи: " + subtask.getTaskDesc() + ".\n"
                + "Статус подзадачи: " + subtask.getStatus() + ".\n"
                + "ID Эпика в рамках которого создана подзадача: " + subtask.getEpicId() + ".\n"
                + "Уникальный ID подзадачи: " + subtaskId + ".\n");
    }

    public void test() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

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
        ArrayList<Subtask> epic1Subtasks = inMemoryTaskManager.getEpicsSubtasks(epic1Id);
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
        for (Integer integer : epic1.getSubtasksIdList()) {
            System.out.println("Сабтаска с ID: " + integer);
            System.out.println(" ");
        }

        System.out.println("\nТесты: проверка работы методов удаления.");
        inMemoryTaskManager.removeTask(task2Id);
        System.out.println(" ");

        System.out.println("\nТесты: проверка удаления эпика, после чего должны удалится его сабтаски.");
        inMemoryTaskManager.removeEpic(epic1Id);
        System.out.println(" ");
        System.out.println("После удаления эпика в списке сабтасок осталось: ");
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println("Сабтаска с ID: " + subtask.getUniqueId() + "\n");
        }

        System.out.println("\nТесты: проверка обновления статуса эпика при удалении сабтасок.\n");
        System.out.println("Для теста взяли эпик с ID: " + epic2.getUniqueId()
                + ". его статус: " + epic2.getStatus() + "\n");
        System.out.println("Для начала мы проверим существование нужных нам для теста сабтасок.\n");
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        List<Subtask> checkWithDebug = inMemoryTaskManager.getSubtasks();
        inMemoryTaskManager.removeSubtask(subtask3Id);
        System.out.println("\nМы удалили сабтаски первого эпика с ID: " + epic2.getUniqueId()
                + ". теперь его статус: " + epic2.getStatus() + "\n");
        System.out.println();

        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        inMemoryTaskManager.getTaskById(task1Id);
        inMemoryTaskManager.getEpicById(epic2Id);
        inMemoryTaskManager.getSubtaskById(subtask3Id);

        System.out.println("\nПытаемся распечатать историю просмотра задач\n");
        inMemoryTaskManager.printHistory();
        inMemoryTaskManager.getEpics();

    }
}
