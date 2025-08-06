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
import org.kr1v.malilearning.client.config.Configs;
import org.kr1v.malilearning.client.config.Hotkeys;
import org.kr1v.malilearning.client.event.InputHandler;

public class InitHandler implements IInitializationHandler
{
	@Override
	public void registerModHandlers()
	{
		ConfigManager.getInstance().registerConfigHandler(MalilearningClient.MOD_ID, new Configs());

		Registry.CONFIG_SCREEN.registerConfigScreenFactory(
				new ModInfo(MalilearningClient.MOD_ID, MalilearningClient.MOD_NAME, GuiConfigs::new)
		);
		Hotkeys.HOTKEY_OPEN_GUI.getKeybind().setCallback(new CallbackOpenConfigGui());
		InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());
	}
	private static class CallbackOpenConfigGui implements IHotkeyCallback {
		public boolean onKeyAction(KeyAction action, IKeybind key) {
			GuiBase.openGui(new GuiConfigs());
			return true;
		}
	}
}