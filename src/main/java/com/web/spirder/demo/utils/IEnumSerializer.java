package com.web.spirder.demo.utils;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author hezifeng
 * @create 2023/3/31 15:25
 */
public class IEnumSerializer extends JsonSerializer<IEnum> {
    public IEnumSerializer() {
    }

    public void serialize(IEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.getValue() instanceof Number) {
            gen.writeNumber(((Number)value.getValue()).longValue());
        } else {
            gen.writeString(value.getValue().toString());
        }

    }

    public Class<IEnum> handledType() {
        return IEnum.class;
    }
}
