package org.kr1v.malilearning.client.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

// this doesn't need to be its own file
public class Hotkeys {
	public static final ConfigHotkey HOTKEY_A = new ConfigHotkey("hotkeyA", "", "Hotkey A comment");

	// comments are optional
	public static final ConfigHotkey HOTKEY_B = new ConfigHotkey("hotkeyB", "");
	public static final ConfigHotkey HOTKEY_C = new ConfigHotkey("hotkeyC", "");

	// you can add default keybinds
	public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("hotkeyOpenMenu", "V,C");

	public static final List<ConfigHotkey> HOTKEY_LIST = List.of(
			HOTKEY_A,
			HOTKEY_B,
			HOTKEY_C,

			HOTKEY_OPEN_GUI
	);
}
