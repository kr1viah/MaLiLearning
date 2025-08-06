package org.kr1v.malilearning.client.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.List;

public class Hotkeys {
	public static final ConfigHotkey HOTKEY_A        = new ConfigHotkey("hotkeyA",        "",             KeybindSettings.PRESS_ALLOWEXTRA, "");
	public static final ConfigHotkey HOTKEY_B   = new ConfigHotkey("hotkeyB",     "",     "");
	public static final ConfigHotkey HOTKEY_C = new ConfigHotkey("hotkeyC",   "",     "");
	public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("hotkeyOpenMenu",   "",     "");

	public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
			HOTKEY_A,
			HOTKEY_B,
			HOTKEY_C,

			HOTKEY_OPEN_GUI
	);
}
