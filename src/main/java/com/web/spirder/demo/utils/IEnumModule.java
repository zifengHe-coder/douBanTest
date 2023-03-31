package com.web.spirder.demo.utils;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import java.util.Collections;

/**
 * @author hezifeng
 * @create 2023/3/31 15:17
 */
public class IEnumModule extends Module {
    public IEnumModule() {
    }

    public String getModuleName() {
        return "IEnum";
    }

    public Version version() {
        return Version.unknownVersion();
    }

    public void setupModule(Module.SetupContext context) {
        context.addBeanDeserializerModifier(new IEnumDeserializerModifier());
        SimpleSerializers serializers = new SimpleSerializers(Collections.singletonList(new IEnumSerializer()));
        context.addSerializers(serializers);
    }
}
