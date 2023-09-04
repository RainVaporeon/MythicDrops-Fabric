package com.spiritlight.adapters.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.IOException;

public interface JacksonSerializable<T> {

    default void serialize(String out) throws IOException {
        this.serialize(new File(out));
    }

    default void deserialize(String in) throws IOException {
        this.deserialize(new File(in));
    }

    /**
     * Serializes this object to the denoted object path
     * @param out the location to serialize to
     * @throws IOException if any I/O exceptions occur
     */
    default void serialize(File out) throws IOException {

        ObjectMapper mapper = new ObjectMapper()
                .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

        mapper.writeValue(out, this);
    }

    /**
     * Deserializes from the denoted path
     * @param in the location to deserialize from
     * @throws IOException if any I/O exceptions occur
     * @apiNote The default implementation deserializes the input file
     * into the current object and returns itself.
     */
    default T deserialize(File in) throws IOException {
        ObjectReader mapper = new ObjectMapper()
                .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .readerForUpdating(this);

        return mapper.readValue(in);
    }
}
