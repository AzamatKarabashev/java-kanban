package ru.practicum.tasks.tester;

import ru.practicum.tasks.task.Epic;
import ru.practicum.tasks.manager.Manager;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.task.Subtask;
import ru.practicum.tasks.task.Task;

import java.util.ArrayList;

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
        Manager manager = new Manager();


        System.out.println("Тесты: создаем и печатаем задачи/эпики/подзадачи.\n");

        Task task1 = new Task("Убраться в гараже", "Уровень невозможно, добро пожаловать", Status.NEW);
        int task1Id = manager.addNewTask(task1);
        taskPrinter(task1, task1Id);

        Task task2 = new Task("Поздравить бабушку с днем рождения", "Забыл = проклят", Status.NEW);
        int task2Id = manager.addNewTask(task2);
        taskPrinter(task2, task2Id);

        Epic epic1 = new Epic("Привести сон в норму", "Нормализация сна", Status.NEW);
        int epic1Id = manager.addNewEpic(epic1);
        epicPrinter(epic1, epic1Id);

        Subtask subtask1 = new Subtask("Купить будильник", "Важная деталь", Status.DONE, epic1Id);
        int subtask1Id = manager.addNewSubtask(subtask1, epic1);
        subtaskPrinter(subtask1, subtask1Id);

        Subtask subtask2 = new Subtask("Запрет на соцсети после 22 часов",
                "Действие осуществляется после покупки будильника", Status.DONE, epic1Id);
        int subtask2Id = manager.addNewSubtask(subtask2, epic1);
        subtaskPrinter(subtask2, subtask2Id);

        Epic epic2 = new Epic("Обрадовать жену", "Хочешь похвалы?", Status.NEW);
        int epic2Id = manager.addNewEpic(epic2);
        epicPrinter(epic2, epic2Id);

        Subtask subtask3 = new Subtask("Починить кран", "Тот день, когда белоручка взял инструменты",
                Status.NEW, epic2Id);
        int subtask3Id = manager.addNewSubtask(subtask3, epic2);
        subtaskPrinter(subtask3, subtask3Id);

        System.out.println("Тесты: печатаем список всех задач для проверки.\n");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
            System.out.println(" ");
        }

        for (Epic epics : manager.getEpics()) {
            System.out.println(epics);
            System.out.println(epics.getStatus());
            System.out.println(" ");
        }

        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
            System.out.println(" ");
        }

        System.out.println("Тесты: печатаем списки подзадач привязанных к эпику.\n");
        ArrayList<Subtask> epic1Subtasks = manager.getEpicsSubtasks(epic1Id);
        for (Subtask epic1Subtask : epic1Subtasks) {
            System.out.println(epic1Subtask);
            System.out.println(" ");
        }

        ArrayList<Subtask> epic2Subtasks = manager.getEpicsSubtasks(epic2Id);
        for (Subtask epic2Subtask : epic2Subtasks) {
            System.out.println(epic2Subtask);
            System.out.println(" ");
        }

        System.out.println("Тесты: обновляем задачки.\n");
        Task task3 = new Task("Тестовая", "Тестовая", Status.NEW);
        manager.updateTask(task1Id, task3);
        System.out.println(" ");

        for (Task task : manager.getTasks()) {
            System.out.println(task);
            System.out.println(" ");
        }

        for (Integer integer : epic1.getSubtasksIdList()) {
            System.out.println(integer);
        }
    }
}
