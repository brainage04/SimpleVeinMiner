package io.github.brainage04.simpleveinminer.command.core;


import io.github.brainage04.simpleveinminer.command.SimpleVeinMinerCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SimpleVeinMinerCommand.initialize(dispatcher);
        });
    }
}