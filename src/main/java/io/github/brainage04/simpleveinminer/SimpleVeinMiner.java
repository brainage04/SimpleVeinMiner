package io.github.brainage04.simpleveinminer;

import io.github.brainage04.simpleveinminer.gamerule.core.ModGameRules;
import io.github.brainage04.simpleveinminer.key.core.ModKeys;
import io.github.brainage04.simpleveinminer.network.core.ModNetworking;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleVeinMiner implements ModInitializer {
	public static final String MOD_ID = "simpleveinminer";
	public static final String MOD_NAME = "SimpleVeinMiner";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("%s initializing...".formatted(MOD_NAME));

        ModGameRules.initialize();
        ModKeys.initialize();
        ModNetworking.initialize();

		LOGGER.info("%s initialized.".formatted(MOD_NAME));
	}
}