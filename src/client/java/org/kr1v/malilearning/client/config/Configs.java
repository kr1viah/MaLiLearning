package org.kr1v.malilearning.client.config;


import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.MessageOutputType;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.MinecraftClient;
import org.kr1v.malilearning.client.MalilearningClient;

import java.io.File;

public class Configs implements IConfigHandler
{
	private static final String CONFIG_FILE_NAME = MalilearningClient.MOD_ID + ".json";

	public static class Generic
	{
		public static final ConfigBoolean A_BOOLEAN_CONFIG            = new ConfigBoolean     ("aBooleanConfig", true, "");
		public static final ConfigInteger AN_INTEGER_CONFIG           = new ConfigInteger     ("anIntegerConfig",  1, 1, 32, "");
		public static final ConfigDouble A_DOUBLE_CONFIG              = new ConfigDouble      ("aDoubleConfig",  3, 1, 5, "");
		public static final ConfigOptionList    AN_OPTION_LIST_CONFIG = new ConfigOptionList  ("anOptionListconfig", MessageOutputType.MESSAGE, "");
		public static final ConfigColor         A_COLOUR_CONFIG       = new ConfigColor       ("aColourConfig", "#80000000", "");
		public static final ConfigString A_STRING_CONFIG              = new ConfigString      ("chatTimeFormat", "[HH:mm:ss]", "");

		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				A_BOOLEAN_CONFIG,
				AN_INTEGER_CONFIG,
				A_DOUBLE_CONFIG,
				AN_OPTION_LIST_CONFIG,
				A_COLOUR_CONFIG,
				A_STRING_CONFIG
		);
	}

	public static class Lists
	{
		public static final ConfigOptionList BLOCK_TYPE_BREAK_RESTRICTION_LIST_TYPE = new ConfigOptionList("blockTypeBreakRestrictionListType", UsageRestriction.ListType.BLACKLIST, "");
		public static final ConfigStringList BLOCK_TYPE_BREAK_RESTRICTION_BLACKLIST = new ConfigStringList("blockTypeBreakRestrictionBlackList", ImmutableList.of("minecraft:budding_amethyst"), "");
		public static final ConfigStringList BLOCK_TYPE_BREAK_RESTRICTION_WHITELIST = new ConfigStringList("blockTypeBreakRestrictionWhiteList", ImmutableList.of(), "");
		public static final ConfigOptionList ENTITY_TYPE_ATTACK_RESTRICTION_LIST_TYPE = new ConfigOptionList("entityTypeAttackRestrictionListType", UsageRestriction.ListType.BLACKLIST, "");

		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				BLOCK_TYPE_BREAK_RESTRICTION_LIST_TYPE,
				BLOCK_TYPE_BREAK_RESTRICTION_BLACKLIST,
				BLOCK_TYPE_BREAK_RESTRICTION_WHITELIST,
				ENTITY_TYPE_ATTACK_RESTRICTION_LIST_TYPE
		);
	}

	public static class Disable
	{
		public static final ConfigBooleanHotkeyed       DISABLE_ARMOR_STAND_RENDERING   = new ConfigBooleanHotkeyed("disableArmorStandRendering",           false, "", "tweakeroo.config.disable.comment.disableArmorStandRendering").translatedName("tweakeroo.config.disable.name.disableArmorStandRendering");
		public static final ConfigBooleanHotkeyed       DISABLE_AXE_STRIPPING           = new ConfigBooleanHotkeyed("disableAxeStripping",                  false, "", "tweakeroo.config.disable.comment.disableAxeStripping").translatedName("tweakeroo.config.disable.name.disableAxeStripping");
		public static final ConfigBooleanHotkeyed       DISABLE_BEACON_BEAM_RENDERING   = new ConfigBooleanHotkeyed("disableBeaconBeamRendering",           false, "", "tweakeroo.config.disable.comment.disableBeaconBeamRendering").translatedName("tweakeroo.config.disable.name.disableBeaconBeamRendering");

		public static final ImmutableList<IHotkeyTogglable> OPTIONS = ImmutableList.of(
				DISABLE_ARMOR_STAND_RENDERING,
				DISABLE_AXE_STRIPPING,
				DISABLE_BEACON_BEAM_RENDERING
		);
	}

	public static class Internal
	{
		public static final ConfigInteger       FLY_SPEED_PRESET                    = new ConfigInteger     ("flySpeedPreset", 0, 0, 3, "tweakeroo.config.internal.comment.flySpeedPreset").translatedName("tweakeroo.config.internal.name.flySpeedPreset");
		public static final ConfigDouble        GAMMA_VALUE_ORIGINAL                = new ConfigDouble      ("gammaValueOriginal", 0, 0, 1, "tweakeroo.config.internal.comment.gammaValueOriginal").translatedName("tweakeroo.config.internal.name.gammaValueOriginal");

		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				FLY_SPEED_PRESET,
				GAMMA_VALUE_ORIGINAL
		);
	}

	public static void loadFromFile()
	{
		File configFile = new File(MinecraftClient.getInstance().runDirectory, "config/" + CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead())
		{
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject())
			{
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", Configs.Generic.OPTIONS);
				ConfigUtils.readConfigBase(root, "Internal", Configs.Internal.OPTIONS);
				ConfigUtils.readConfigBase(root, "Lists", Configs.Lists.OPTIONS);
				ConfigUtils.readHotkeyToggleOptions(root, "DisableHotkeys", "DisableToggles", Disable.OPTIONS);
			}
		}
	}

	public static void saveToFile()
	{
		File dir = new File(MinecraftClient.getInstance().runDirectory, "config");

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
		{
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Internal", Configs.Internal.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.OPTIONS);
			ConfigUtils.writeHotkeyToggleOptions(root, "DisableHotkeys", "DisableToggles", Disable.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
		}
	}

	@Override
	public void load()
	{
		loadFromFile();
	}

	@Override
	public void save()
	{
		saveToFile();
	}
}