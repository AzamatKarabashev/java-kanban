package ru.practicum.tasks.converter;

import com.google.gson.*;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.task.Subtask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskDeserializer implements JsonDeserializer<Subtask> {
    @Override
    public Subtask deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Subtask subtask = new Subtask();
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Integer id = jsonObject.get("id").getAsInt();
        Status status = Status.valueOf(jsonObject.get("status").getAsString());
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setId(id);
        subtask.setStatus(status);
        if (!(jsonObject.get("epicId") == null)) {
            subtask.setEpicId(Integer.parseInt(jsonObject.get("epicId").getAsString()));
        }
        if (!(jsonObject.get("startTime") == null)) {
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            subtask.setStartTime(startTime);
        }
        if (!(jsonObject.get("duration") == null)) {
            Duration duration = Duration.parse(jsonObject.get("duration").getAsString());
            subtask.setDuration(duration);
        }
        return subtask;
    }
}
