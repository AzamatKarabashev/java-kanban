package ru.practicum.tasks.manager.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException (String message){
        super (message);
    }
}
