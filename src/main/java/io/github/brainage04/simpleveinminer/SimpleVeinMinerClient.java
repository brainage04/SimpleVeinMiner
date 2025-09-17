package io.github.brainage04.simpleveinminer;

import io.github.brainage04.simpleveinminer.key.core.ModKeys;
import io.github.brainage04.simpleveinminer.network.VeinMinePayload;
import io.github.brainage04.simpleveinminer.network.core.ModNetworking;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleVeinMinerClient implements ClientModInitializer {
	public static final String MOD_ID = "simpleveinminer";
	public static final String MOD_NAME = "SimpleVeinMiner";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("%s initializing...".formatted(MOD_NAME));

        ModKeys.initialize();

		LOGGER.info("%s initialized.".formatted(MOD_NAME));
	}
}