package io.github.brainage04.simpleveinminer.key.core;

import io.github.brainage04.simpleveinminer.SimpleVeinMinerClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeys {
    public static final KeyBinding veinMineKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.%s.veinMineKey".formatted(SimpleVeinMinerClient.MOD_ID),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "key.category.%s".formatted(SimpleVeinMinerClient.MOD_ID)
    ));

    public static void initialize() {

    }
}