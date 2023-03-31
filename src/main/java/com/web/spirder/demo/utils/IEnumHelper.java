package com.web.spirder.demo.utils;

import com.baomidou.mybatisplus.core.enums.IEnum;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hezifeng
 * @create 2023/3/31 15:22
 */
public class IEnumHelper<EnumType extends Enum<EnumType> & IEnum<ValueType>, ValueType extends Serializable> {
    private Map<ValueType, EnumType> reverseMap;
    private final Class<EnumType> enumTypeClass;
    private static ConcurrentHashMap<Class<? extends Enum>, IEnumHelper<? extends Enum, ?>> cachedValuedEnumHelpers = new ConcurrentHashMap();

    public IEnumHelper(Class<EnumType> enumClazz) {
        this.enumTypeClass = enumClazz;
        EnumSet<EnumType> allEnums = EnumSet.allOf(enumClazz);
        this.reverseMap = new HashMap();
        Iterator var3 = allEnums.iterator();

        while(var3.hasNext()) {
            EnumType enumValue = (EnumType) var3.next();
            ValueType value = (enumValue).getValue();
            if (this.reverseMap.get(value) != null) {
                throw new IEnumHelper.DuplicatedEnumValueError(String.format("枚举数值<%s>定义重复，将无法影响枚举数值到枚举值的反向映射", value.toString()));
            }

            this.reverseMap.put(value, enumValue);
        }

        this.reverseMap = Collections.unmodifiableMap(this.reverseMap);
    }

    public EnumType enumForValue(ValueType value) throws IEnumHelper.NoSuchEnumWithValueException {
        EnumType enumValue = (EnumType)this.reverseMap.get(value);
        if (enumValue == null) {
            throw new IEnumHelper.NoSuchEnumWithValueException(String.format("无法找到对应值<%s>的枚举值（枚举类型：%s）", value.toString(), this.enumTypeClass.getName()));
        } else {
            return enumValue;
        }
    }

    public static <EnumType extends Enum<EnumType> & IEnum<ValueType>, ValueType extends Serializable> List<EnumType> getAllEnums(Class<EnumType> enumClazz) {
        IEnumHelper<EnumType, ValueType> helper = (IEnumHelper)cachedValuedEnumHelpers.get(enumClazz);
        if (helper == null) {
            helper = new IEnumHelper(enumClazz);
            cachedValuedEnumHelpers.put(enumClazz, helper);
        }

        return new ArrayList(helper.reverseMap.values());
    }

    public static <EnumType extends Enum<EnumType> & IEnum<ValueType>, ValueType extends Serializable> EnumType unsafeGetEnumForValue(Class<EnumType> enumClazz, Object value) throws IEnumHelper.NoSuchEnumWithValueException {
        return getEnumForValue(enumClazz, (ValueType)value);
    }

    public static <EnumType extends Enum<EnumType> & IEnum<ValueType>, ValueType extends Serializable> EnumType getEnumForValue(Class<EnumType> enumClazz, ValueType value) throws IEnumHelper.NoSuchEnumWithValueException {
        IEnumHelper<EnumType, ValueType> helper = (IEnumHelper)cachedValuedEnumHelpers.get(enumClazz);
        if (helper == null) {
            helper = new IEnumHelper(enumClazz);
            cachedValuedEnumHelpers.put(enumClazz, helper);
        }

        return helper.enumForValue(value);
    }

    public static <EnumType extends Enum<EnumType> & IEnum<ValueType>, ValueType extends Serializable> Class<?> getValueType(Class<EnumType> enumClazz) {
        return unsafeGetValueType(enumClazz);
    }

    public static Class<?> unsafeGetValueType(Class<?> enumClazz) {
        ParameterizedType superclass = (ParameterizedType)enumClazz.getGenericInterfaces()[0];
        Type valueType = superclass.getActualTypeArguments()[0];
        return (Class)valueType;
    }

    public static class DuplicatedEnumValueError extends Error {
        private static final long serialVersionUID = 1705033679905600263L;

        DuplicatedEnumValueError(String message) {
            super(message);
        }
    }

    public static class NoSuchEnumWithValueException extends Exception {
        private static final long serialVersionUID = 1705033679905600263L;

        NoSuchEnumWithValueException(String message) {
            super(message);
        }
    }
}
