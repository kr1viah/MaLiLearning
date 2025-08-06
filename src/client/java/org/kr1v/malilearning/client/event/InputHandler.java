package org.kr1v.malilearning.client.event;

import net.minecraft.client.MinecraftClient;
import fi.dy.masa.malilib.hotkeys.*;
import fi.dy.masa.malilib.util.GuiUtils;
import org.kr1v.malilearning.client.MalilearningClient;
import org.kr1v.malilearning.client.config.Hotkeys;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
	private static final InputHandler INSTANCE = new InputHandler();

	private InputHandler() {
		super();
	}

	public static InputHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void addKeysToMap(IKeybindManager manager) {
		for (IHotkey hotkey : Hotkeys.HOTKEY_LIST) {
			manager.addKeybindToMap(hotkey.getKeybind());
		}
	}

	@Override
	public void addHotkeys(IKeybindManager manager) {
		manager.addHotkeysForCategory(MalilearningClient.MOD_NAME, "Generic", Hotkeys.HOTKEY_LIST);
	}

	@Override
	public boolean onKeyInput(int keyCode, int scanCode, int modifiers, boolean eventKeyState) {
		return false;
	}

	@Override
	public boolean onMouseClick(int mouseX, int mouseY, int eventButton, boolean eventButtonState) {
		MinecraftClient mc = MinecraftClient.getInstance();

		if (mc.world == null || mc.player == null || mc.interactionManager == null || mc.crosshairTarget == null ||
				GuiUtils.getCurrentScreen() != null)
			return false;

		return false;
	}

	@Override
	public boolean onMouseScroll(int mouseX, int mouseY, double dWheel) {
		return false;
	}
}