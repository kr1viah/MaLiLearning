package org.kr1v.malilearning.client.config;


import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.MessageOutputType;
import net.minecraft.client.MinecraftClient;
import org.kr1v.malilearning.client.MalilearningClient;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = MalilearningClient.MOD_ID + ".json";

	public static class Generic {
		public static final ConfigBoolean    A_BOOLEAN_CONFIG =      new ConfigBoolean("aBooleanConfig", true, "");
		public static final ConfigInteger    AN_INTEGER_CONFIG =     new ConfigInteger("anIntegerConfig", 1, 1, 32, "");
		public static final ConfigDouble     A_DOUBLE_CONFIG =       new ConfigDouble("aDoubleConfig", 3, 1, 5, "");
		public static final ConfigOptionList AN_OPTION_LIST_CONFIG = new ConfigOptionList("anOptionListconfig", MessageOutputType.MESSAGE, "");
		public static final ConfigColor      A_COLOUR_CONFIG =       new ConfigColor("aColourConfig", "#80000000", "");
		public static final ConfigString     A_STRING_CONFIG =       new ConfigString("chatTimeFormat", "Some string", "");

		public static final List<IConfigBase> OPTIONS = List.of(
				A_BOOLEAN_CONFIG,
				AN_INTEGER_CONFIG,
				A_DOUBLE_CONFIG,
				AN_OPTION_LIST_CONFIG,
				A_COLOUR_CONFIG,
				A_STRING_CONFIG
		);
	}

	public static class Lists {
		public static final ConfigStringList STRING_LIST_1 = new ConfigStringList("String list 1", ImmutableList.of("Default values", "go here"), "");
		public static final ConfigStringList STRING_LIST_2 = new ConfigStringList("Another string list", ImmutableList.of(), "This one has a comment!");

		public static final List<IConfigBase> OPTIONS = List.of(
				STRING_LIST_1,
				STRING_LIST_2
		);
	}

	public static void loadFromFile() {
		File configFile = new File(MinecraftClient.getInstance().runDirectory, "config/" + CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", Configs.Generic.OPTIONS);
				ConfigUtils.readConfigBase(root, "Lists", Configs.Lists.OPTIONS);
				ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
			}
		}
	}

	public static void saveToFile() {
		File dir = new File(MinecraftClient.getInstance().runDirectory, "config");

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);

			JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
		}
	}

	@Override
	public void load() {
		loadFromFile();
	}

	@Override
	public void save() {
		saveToFile();
	}
}