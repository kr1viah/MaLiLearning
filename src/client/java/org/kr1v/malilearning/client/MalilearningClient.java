package org.kr1v.malilearning.client;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;

public class MalilearningClient implements ClientModInitializer {
	public static final String MOD_ID = "malilearning";
	public static final String MOD_NAME = "MaLiLearning";

	@Override
	public void onInitializeClient() {
		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
	}
}
