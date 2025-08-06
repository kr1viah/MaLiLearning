package org.kr1v.malilearning.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.kr1v.malilearning.client.config.Configs;
import org.kr1v.malilearning.client.config.Hotkeys;

public class RegisterHotkeys {
	public static void registerHotkeys() {
		Hotkeys.HOTKEY_A.getKeybind().setCallback((keyAction, iKeybind) -> {
			sendMessage("Keybind A pressed! aBooleanConfig value is: " + Configs.Generic.A_BOOLEAN_CONFIG.getBooleanValue());
			return true;
		});
		Hotkeys.HOTKEY_B.getKeybind().setCallback((keyAction, iKeybind) -> {
			sendMessage("Keybind B pressed!");
			return true;
		});
		Hotkeys.HOTKEY_C.getKeybind().setCallback((keyAction, iKeybind) -> {
			sendMessage("Keybind C pressed!");
			return true;
		});
	}
	private static void sendMessage(String message) {
		var player = MinecraftClient.getInstance().player;
		if (player != null) {
			player.sendMessage(Text.literal(message), true);
		} else {
			throw new RuntimeException("Help it doid");
		}
	}
}
