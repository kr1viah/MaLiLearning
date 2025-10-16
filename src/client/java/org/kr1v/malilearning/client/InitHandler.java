package org.kr1v.malilearning.client;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.kr1v.malilearning.client.config.Configs;
import org.kr1v.malilearning.client.config.Hotkeys;
import org.kr1v.malilearning.client.event.InputHandler;

import java.util.List;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        // register our config handler
        ConfigManager.getInstance().registerConfigHandler(MalilearningClient.MOD_ID, new Configs());

        // register our screen in the MaLiLib config switcher
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(
                new ModInfo(MalilearningClient.MOD_ID, MalilearningClient.MOD_NAME, GuiConfigs::new)
        );
        // this hotkey is special
        Hotkeys.HOTKEY_OPEN_GUI.getKeybind().setCallback(new CallbackOpenConfigGui());

        // register hotkeys
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        // register their callbacks
        Hotkeys.HOTKEY_A.getKeybind().setCallback((keyAction, iKeybind) -> { // you can use a lambda for callbacks
            boolean aBooleanConfig = Configs.Generic.A_BOOLEAN_CONFIG.getBooleanValue();       // you can get a config value as such
            sendMessage("Keybind A pressed! aBooleanConfig value is: " + aBooleanConfig);      // and use it like a normal boolean/double/whatever
            return true;
        });
        Hotkeys.HOTKEY_B.getKeybind().setCallback((keyAction, iKeybind) -> {
            sendMessage("Keybind B pressed!");
            return true;
        });
        Hotkeys.HOTKEY_C.getKeybind().setCallback(new HotkeyCCallback()); // or make a class that implements IHotkeyCallback
    }

    private static class HotkeyCCallback implements IHotkeyCallback {
        @Override
        public boolean onKeyAction(KeyAction keyAction, IKeybind iKeybind) { // this class must implement onKeyAction
            sendMessage("Keybind C pressed!");
            List<String> stringList = Configs.Lists.STRING_LIST_1.getStrings();
            for (String s : stringList) {
                sendMessage(s);
            }
            return true;
        }
    }

    private static void sendMessage(String message) {
        var player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.sendMessage(Text.literal(message), false);
        }
    }

    private static class CallbackOpenConfigGui implements IHotkeyCallback {
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            GuiBase.openGui(new GuiConfigs());
            return true;
        }
    }
}