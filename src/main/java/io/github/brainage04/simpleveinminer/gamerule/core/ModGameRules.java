package io.github.brainage04.simpleveinminer.gamerule.core;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRules {
    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }

    public static final GameRules.Key<GameRules.BooleanRule> ENABLE_VEIN_MINING =
            register("enableVeinMining", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    public static void initialize() {

    }
}
