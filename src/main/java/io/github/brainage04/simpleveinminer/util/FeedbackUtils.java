package io.github.brainage04.simpleveinminer.util;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class FeedbackUtils {
    public static void sendFeedback(ServerCommandSource source, String message) {
        source.sendFeedback(() -> Text.literal(message), false);
    }
}