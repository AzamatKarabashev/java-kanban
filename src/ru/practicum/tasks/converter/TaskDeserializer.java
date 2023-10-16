package ru.practicum.tasks.converter;

import com.google.gson.*;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.task.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDeserializer implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Task task = new Task();
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Integer id = jsonObject.get("id").getAsInt();
        Status status = Status.valueOf(jsonObject.get("status").getAsString());
        task.setName(name);
        task.setDescription(description);
        task.setId(id);
        task.setStatus(status);
        if (!(jsonObject.get("startTime") == null)) {
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            task.setStartTime(startTime);
        }
        if (!(jsonObject.get("duration") == null)) {
            Duration duration = Duration.parse(jsonObject.get("duration").getAsString());
            task.setDuration(duration);
        }
        return task;
    }
}