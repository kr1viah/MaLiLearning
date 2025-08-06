package org.kr1v.malilearning.client;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import org.kr1v.malilearning.client.config.Configs;
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
		InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());
	}
}