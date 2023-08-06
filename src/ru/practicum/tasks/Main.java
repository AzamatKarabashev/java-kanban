package ru.practicum.tasks;

import ru.practicum.tasks.tester.Tester;

public class Main {

    /*
    Пятый коммит:
    добавили приватное поле ArrayList в класс Epic, котрое хранит в себе
    idSubtask которые к нему относятся;
    добавили метод реализующий пополнение указанного поля;
    теперь метод addNewSubtask также добавляет в указанный ArrayList свой id;
    изменили override hashCode и equals с учетом внесенного поля в класс Epic;

    p.s.:
    К сожалению теперь вызов метода addNewSubtask также требует сам Epic к которому относится...
    pp.s.:
    Надо просто перейти на HashMap-у и спокойно искать по ключам,
    которые будут являтся id-шниками тасок/эпиков/подзадач...
     */

    public static void main(String[] args) {
        Tester tester = new Tester();
        tester.test();
    }
}
