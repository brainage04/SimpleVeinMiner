package io.github.brainage04.simpleveinminer.network.core;

import io.github.brainage04.simpleveinminer.SimpleVeinMinerClient;
import io.github.brainage04.simpleveinminer.gamerule.core.ModGameRules;
import io.github.brainage04.simpleveinminer.network.VeinMinePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModNetworking {
    public static final Identifier VEIN_MINE_PACKET_ID = Identifier.of(SimpleVeinMinerClient.MOD_ID, "vein_mine");

    // decrement durability and add exhaustion
    // returns true if tool broke
    private static boolean onBlockBreak(ServerPlayerEntity player) {
        if (player.isCreative()) return false;

        player.addExhaustion(0.005F);

        ItemStack held = player.getMainHandStack();
        if (held.isDamageable())  {
            AtomicBoolean toolBroken = new AtomicBoolean(false);
            held.damage(1, player.getWorld(), player, p -> toolBroken.set(true));
            return toolBroken.get();
        }

        return false;
    }

    private static void veinMine(ServerPlayerEntity player, BlockPos start) {
        GameRules rules = player.getWorld().getGameRules();
        int maxBlocks = rules.getInt(ModGameRules.MAX_BLOCKS);
        int maxVisitedBlocks = rules.getInt(ModGameRules.MAX_VISITED_BLOCKS);

        World world = player.getWorld();
        BlockState targetState = world.getBlockState(start);

        if (targetState.isAir()) return;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);

        int blocksBroken = 1;
        while (!queue.isEmpty() && blocksBroken < maxBlocks && visited.size() < maxVisitedBlocks) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            BlockState state = world.getBlockState(pos);
            if (!state.isOf(targetState.getBlock())) continue;

            if (!pos.equals(start)) {
                world.breakBlock(pos, !player.isInCreativeMode(), player);
                // stop immediately if tool broke
                if (onBlockBreak(player)) return;
                blocksBroken++;
            }

            // add neighbors in 3x3x3 area (horizontal/vertical/diagonal neighbours)
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        // edge case
                        if (x == 0 && y == 0 && z == 0) continue;

                        BlockPos neighbor = pos.add(x, y, z);
                        if (!visited.contains(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(VeinMinePayload.ID, VeinMinePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(VeinMinePayload.ID, (payload, context) -> {
            MinecraftServer server = context.server();
            boolean veinMiningEnabled = server.getGameRules().getBoolean(ModGameRules.ENABLE_VEIN_MINING);
            if (!veinMiningEnabled) return;

            ServerPlayerEntity player = server.getPlayerManager().getPlayer(payload.playerUuid());
            if (player == null) return;

            BlockPos startPos = payload.blockPos();

            veinMine(player, startPos);
        });
    }
}
