package ru.practicum.tasks.converter;

import com.google.gson.*;
import ru.practicum.tasks.model.Status;
import ru.practicum.tasks.model.task.Epic;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Epic epic = new Epic();
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Integer id = jsonObject.get("id").getAsInt();
        Status status = Status.valueOf(jsonObject.get("status").getAsString());
        epic.setName(name);
        epic.setDescription(description);
        epic.setId(id);
        epic.setStatus(status);
        if (!(jsonObject.get("startTime") == null)) {
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            epic.setStartTime(startTime);
        }
        if (!(jsonObject.get("duration") == null)) {
            Duration duration = Duration.parse(jsonObject.get("duration").getAsString());
            epic.setDuration(duration);
        }
        if (!(jsonObject.get("endTime") == null)) {
            LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("ednTime").getAsString());
            epic.setEndTime(endTime);
        }
        return epic;
    }
}
