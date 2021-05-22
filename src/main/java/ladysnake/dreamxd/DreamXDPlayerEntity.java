package ladysnake.dreamxd;

import baritone.api.fakeplayer.FakeServerPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class DreamXDPlayerEntity extends FakeServerPlayerEntity {
    public DreamXDPlayerEntity(EntityType<? extends PlayerEntity> type, ServerWorld world) {
        super(type, world, new GameProfile(UUID.fromString("b38e3783-a1e1-4ee3-9b3e-fb18ccf3ecea"), "DreamXD"));
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new PlayerSpawnS2CPacket(this);
    }
}
