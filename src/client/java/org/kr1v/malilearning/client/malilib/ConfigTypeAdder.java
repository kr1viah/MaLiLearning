package org.kr1v.malilearning.client.malilib;

import com.chocohead.mm.api.ClassTinkerers;

public class ConfigTypeAdder implements Runnable {
    @Override
    public void run() {
        try {
            var enumBuilder = ClassTinkerers.enumBuilder(
                    "fi.dy.masa.malilib.config.ConfigType",
                    String.class,
                    com.mojang.serialization.Codec.class
            );

            enumBuilder.addEnum("STRING_MAP", "string_map", null);
            enumBuilder.build();
        } catch (Throwable t) {
            throw new RuntimeException("ConfigType enum extension failed", t);
        }
    }
}