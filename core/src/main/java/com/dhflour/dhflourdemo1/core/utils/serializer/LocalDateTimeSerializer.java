package com.dhflour.dhflourdemo1.core.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            gen.writeNumber(value.toEpochSecond(ZoneOffset.UTC));
        } catch (IOException e) {
            gen.writeNumber(0);
        }
    }
}
