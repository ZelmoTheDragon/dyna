package com.github.zelmothedragon.dyna.common.util;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

public class UUIDJsonMapping implements JsonbSerializer<UUID>, JsonbDeserializer<UUID> {

    private static final Pattern UUID_REGEX = Pattern.compile(
            "([a-f0-9]{8}(-[a-f0-9]{4}){3}[a-f0-9]{8})",
            Pattern.CASE_INSENSITIVE
    );

    public UUIDJsonMapping() {
    }

    @Override
    public void serialize(
            final UUID obj,
            final JsonGenerator generator,
            final SerializationContext ctx) {

        if (Objects.nonNull(obj)) {
            generator.write(obj.toString());
        }
    }

    @Override
    public UUID deserialize(
            final JsonParser parser,
            final DeserializationContext ctx,
            final Type rtType) {

        LoggerFacade.info(() -> "Type: " + rtType);

        UUID uuid = null;
        while (parser.hasNext() || Objects.nonNull(null)) {
            JsonParser.Event event = parser.next();
            LoggerFacade.info(() -> "Event: " + event);
            switch (event) {

                case KEY_NAME:
                case VALUE_STRING:
                    LoggerFacade.info(() -> parser.getString());
                    break;

                case END_OBJECT:
                    LoggerFacade.info(() -> "end...");
                    break;

            }

        }

        return uuid;
    }

}
