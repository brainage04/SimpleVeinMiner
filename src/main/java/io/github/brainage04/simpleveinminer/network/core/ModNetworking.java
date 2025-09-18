package io.github.brainage04.simpleveinminer.network.core;

import io.github.brainage04.simpleveinminer.SimpleVeinMiner;
import io.github.brainage04.simpleveinminer.SimpleVeinMinerClient;
import io.github.brainage04.simpleveinminer.gamerule.core.ModGameRules;
import io.github.brainage04.simpleveinminer.network.VeinMinePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.*;
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

    private static void veinMine(ServerPlayerEntity player, BlockPos start, BlockState targetState) {
        GameRules rules = player.getWorld().getGameRules();
        int maxBlocks = rules.getInt(ModGameRules.MAX_BLOCKS);
        int maxVisitedBlocks = rules.getInt(ModGameRules.MAX_VISITED_BLOCKS);

        World world = player.getWorld();

        if (targetState.isAir()) return;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);

        ItemEntity startEntity = getStartEntity(start, world);

        int blocksBroken = 1;
        while (!queue.isEmpty() && blocksBroken < maxBlocks && visited.size() < maxVisitedBlocks) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            if (!pos.equals(start)) {
                BlockState state = world.getBlockState(pos);
                if (!state.isOf(targetState.getBlock())) continue;

                boolean dropped = world.breakBlock(pos, !player.isInCreativeMode(), player);

                // add the dropped item to the stack at the starting position for convenience
                if (dropped && startEntity != null) {
                    Box box = new Box(pos);
                    List<ItemEntity> possibleEntities = world.getEntitiesByClass(ItemEntity.class, box, entity -> true);

                    if (possibleEntities.size() == 1) {
                        ItemEntity entity = possibleEntities.getFirst();

                        ItemStack stack = startEntity.getStack();
                        stack.increment(entity.getStack().getCount());
                        startEntity.setStack(stack);

                        entity.discard();
                    } else if (!possibleEntities.isEmpty()) {
                        SimpleVeinMiner.LOGGER.error("ItemEntity list size for vein mined block is not 0 or 1 but {} - this shouldn't happen!", possibleEntities.size());
                    }
                }

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

    private static ItemEntity getStartEntity(BlockPos start, World world) {
        Box box = new Box(start).expand(1, 1, 1);
        List<ItemEntity> possibleEntities = world.getEntitiesByClass(ItemEntity.class, box, entity -> true);

        if (possibleEntities.size() == 1) {
            return possibleEntities.getFirst();
        } else if (!possibleEntities.isEmpty()) {
            SimpleVeinMiner.LOGGER.error("Starting ItemEntity list size is not 0 or 1 but {} - this shouldn't happen!", possibleEntities.size());
            // todo: spawn stack with size 0 maybe?
        }

        return null;
    }

    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(VeinMinePayload.ID, VeinMinePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(VeinMinePayload.ID, (payload, context) -> {
            MinecraftServer server = context.server();
            boolean veinMiningEnabled = server.getGameRules().getBoolean(ModGameRules.ENABLE_VEIN_MINING);
            if (!veinMiningEnabled) return;

            ServerPlayerEntity player = server.getPlayerManager().getPlayer(payload.playerUuid());
            if (player == null) return;

            BlockPos pos = payload.blockPos();
            BlockState state = player.getWorld().getBlockState(pos);

            server.execute(() -> veinMine(player, pos, state));
        });
    }
}
