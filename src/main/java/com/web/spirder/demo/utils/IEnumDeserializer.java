package com.web.spirder.demo.utils;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author hezifeng
 * @create 2023/3/31 15:21
 */
public class IEnumDeserializer<ValueType extends Serializable, EnumType extends Enum<EnumType> & IEnum<ValueType>> extends JsonDeserializer<EnumType> {
    private final Class<EnumType> valuedEnumTypeClass;

    IEnumDeserializer(Class<EnumType> valuedEnumTypeClass) {
        this.valuedEnumTypeClass = valuedEnumTypeClass;
    }

    public EnumType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Class valueType = IEnumHelper.getValueType(this.valuedEnumTypeClass);

        try {
            if (Integer.class.equals(valueType)) {
                if (p.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
                    throw new JsonParseException(p, String.format("%s 的值必须为 <%s>.", p.getParsingContext().getCurrentName(), valueType.getName()));
                } else {
                    int val = p.getValueAsInt();
                    return IEnumHelper.unsafeGetEnumForValue(this.valuedEnumTypeClass, val);
                }
            } else if (Long.class.equals(valueType)) {
                if (p.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
                    throw new JsonParseException(p, String.format("%s 的值必须为 <%s>.", p.getParsingContext().getCurrentName(), valueType.getName()));
                } else {
                    long val = p.getValueAsLong();
                    return IEnumHelper.unsafeGetEnumForValue(this.valuedEnumTypeClass, val);
                }
            } else {
                String val = p.getValueAsString();
                return StringUtils.isEmpty(val) ? null : IEnumHelper.unsafeGetEnumForValue(this.valuedEnumTypeClass, val);
            }
        } catch (IEnumHelper.NoSuchEnumWithValueException var6) {
            throw new JsonParseException(p, String.format("%s 的值 <%s>不合法.", p.getParsingContext().getCurrentName(), p.getValueAsString()));
        }
    }

    public Class<?> handledType() {
        return this.valuedEnumTypeClass;
    }
}
