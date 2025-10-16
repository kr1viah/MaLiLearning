package org.kr1v.malilearning.client;

import com.chocohead.mm.api.ClassTinkerers;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import org.kr1v.malilearning.client.malilib.config.options.ConfigStringMap;

import java.lang.reflect.Field;

public class MalilearningClient implements ClientModInitializer {
    public static final String MOD_ID = "malilearning";
    public static final String MOD_NAME = "MaLiLearning";

    @Override
    public void onInitializeClient() {
        setCodec();
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
    }

    public static void setCodec() {
        try {
            ConfigType value = ClassTinkerers.getEnum(ConfigType.class, "STRING_MAP");

            Field codecField = ConfigType.class.getDeclaredField("codec");
            codecField.setAccessible(true);

            try {
                codecField.set(value, ConfigStringMap.CODEC);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Could not set codec!", e);
            }
        } catch (NoSuchFieldException nsf) {
            throw new RuntimeException("Field 'codec' not found on ConfigType", nsf);
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Enum constant STRING_MAP not found (make sure it was added earlier)", iae);
        } catch (Exception e) {
            throw new RuntimeException("Failed to patch ConfigType.codec for STRING_MAP", e);
        }
    }
}
