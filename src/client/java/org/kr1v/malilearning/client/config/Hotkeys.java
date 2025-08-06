package org.kr1v.malilearning.client.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.List;

public class Hotkeys {
	public static final ConfigHotkey ACCURATE_BLOCK_PLACEMENT_IN        = new ConfigHotkey("accurateBlockPlacementInto",        "",             KeybindSettings.PRESS_ALLOWEXTRA, "");
	public static final ConfigHotkey BREAKING_RESTRICTION_MODE_COLUMN   = new ConfigHotkey("breakingRestrictionModeColumn",     "",     "");
	public static final ConfigHotkey BREAKING_RESTRICTION_MODE_DIAGONAL = new ConfigHotkey("breakingRestrictionModeDiagonal",   "",     "");
	public static final ConfigHotkey FLEXIBLE_BLOCK_PLACEMENT_ADJACENT  = new ConfigHotkey("flexibleBlockPlacementAdjacent",    "",             KeybindSettings.PRESS_ALLOWEXTRA, "");
	public static final ConfigHotkey FLEXIBLE_BLOCK_PLACEMENT_OFFSET    = new ConfigHotkey("flexibleBlockPlacementOffset",      "LEFT_CONTROL", KeybindSettings.PRESS_ALLOWEXTRA, "");
	public static final ConfigHotkey FLEXIBLE_BLOCK_PLACEMENT_ROTATION  = new ConfigHotkey("flexibleBlockPlacementRotation",    "LEFT_ALT",     KeybindSettings.PRESS_ALLOWEXTRA, "");

	public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
			ACCURATE_BLOCK_PLACEMENT_IN,
			BREAKING_RESTRICTION_MODE_COLUMN,
			BREAKING_RESTRICTION_MODE_DIAGONAL,
			FLEXIBLE_BLOCK_PLACEMENT_ADJACENT,
			FLEXIBLE_BLOCK_PLACEMENT_OFFSET,
			FLEXIBLE_BLOCK_PLACEMENT_ROTATION
	);
}
