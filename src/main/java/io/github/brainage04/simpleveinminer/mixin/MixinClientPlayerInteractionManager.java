package io.github.brainage04.simpleveinminer.mixin;

import io.github.brainage04.simpleveinminer.key.core.ModKeys;
import io.github.brainage04.simpleveinminer.network.VeinMinePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private void simpleveinminer$breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!ModKeys.veinMineKey.isPressed()) return;

        ClientPlayerEntity player = client.player;
        if (player == null) return;

        ClientPlayNetworking.send(new VeinMinePayload(player.getUuid(), pos));
    }
}
