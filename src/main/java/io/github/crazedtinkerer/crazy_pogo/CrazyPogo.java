package io.github.crazedtinkerer.crazy_pogo;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrazyPogo implements ModInitializer {
	public static final String MOD_ID = "crazy_pogo";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
	}
}
