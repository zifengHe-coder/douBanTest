package com.web.spirder.demo.utils;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author hezifeng
 * @create 2023/3/31 15:18
 */
public class IEnumDeserializerModifier extends BeanDeserializerModifier {
    public IEnumDeserializerModifier() {
    }

    public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        Class<?> clazz = type.getRawClass();
        return (JsonDeserializer)(IEnum.class.isAssignableFrom(clazz) ? new IEnumDeserializer(clazz) : super.modifyEnumDeserializer(config, type, beanDesc, deserializer));
    }
}
