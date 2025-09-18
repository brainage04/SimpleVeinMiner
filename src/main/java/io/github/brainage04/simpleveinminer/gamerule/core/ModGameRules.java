package io.github.brainage04.simpleveinminer.gamerule.core;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;

public class ModGameRules {
    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        GameRules.Key<T> key = GameRuleRegistry.register(name, category, type);
        REGISTERED.add(key);
        return key;
    }

    public static final List<GameRules.Key<?>> REGISTERED = new ArrayList<>();

    public static final GameRules.Key<GameRules.BooleanRule> ENABLE_VEIN_MINING =
            register("enableVeinMining", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule> MAX_BLOCKS =
            register("maxBlocks", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(64));
    public static final GameRules.Key<GameRules.IntRule> MAX_VISITED_BLOCKS =
            register("maxVisitedBlocks", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(512));

    public static void initialize() {

    }
}
