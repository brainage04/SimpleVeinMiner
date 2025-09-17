package io.github.brainage04.simpleveinminer.network;

import io.github.brainage04.simpleveinminer.network.core.ModNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public record VeinMinePayload(UUID playerUuid, BlockPos blockPos) implements CustomPayload {
    public static final CustomPayload.Id<VeinMinePayload> ID = new CustomPayload.Id<>(ModNetworking.VEIN_MINE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, VeinMinePayload> CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, VeinMinePayload::playerUuid,
            BlockPos.PACKET_CODEC, VeinMinePayload::blockPos,
            VeinMinePayload::new
    );
 
    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}