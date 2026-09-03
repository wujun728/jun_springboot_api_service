package com.yuyaogc.lowcode.engine.jsonrpc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

/**
 * json rpc id
 *
 *
 */
@JsonSerialize(using = RpcIdJsonSerializer.class)
@JsonDeserialize(using = RpcIdJsonDeserializer.class)
public class RpcId {
    Object id;

    public RpcId(String id) {
        this.id = id;
    }

    public RpcId(long id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class RpcIdJsonDeserializer extends JsonDeserializer<RpcId> {

    @Override
    public RpcId deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken token = parser.currentToken();
        if (token == JsonToken.VALUE_STRING) {
            return new RpcId(parser.getText());
        }
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return new RpcId(parser.getLongValue());
        }
        if (token == JsonToken.VALUE_NULL) {
            return new RpcId(null);
        }
        throw new IOException("id must be string or long value");
    }
}

class RpcIdJsonSerializer extends JsonSerializer<RpcId> {

    @Override
    public void serialize(RpcId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.id instanceof String) {
            gen.writeString((String) value.id);
        } else if (value.id instanceof Long) {
            gen.writeNumber((long) value.id);
        } else {
            gen.writeNull();
        }
    }
}
