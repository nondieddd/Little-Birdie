package org.nondied.banda_de_trapo.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DiscoFloorBlockEntity extends BlockEntity {

    private int mode = 1;
    private BlockPos centerPos = null;

    public DiscoFloorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DISCO_FLOOR_ENTITY_TYPE, pos, state);
    }

    public void setMode(int mode) {
        this.mode = mode;
        this.markDirty();
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(this.getPos());
        }
    }

    public int getMode() {
        return this.mode;
    }

    public void setCenterPos(BlockPos centerPos) {
        this.centerPos = centerPos;
        this.markDirty();
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(this.getPos());
        }
    }

    public BlockPos getCenterPos() {
        return this.centerPos != null ? this.centerPos : this.getPos();
    }

    public boolean hasCenterPos() {
        return this.centerPos != null;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("disco_mode", this.mode);
        if (this.centerPos != null) {
            nbt.putInt("center_x", this.centerPos.getX());
            nbt.putInt("center_y", this.centerPos.getY());
            nbt.putInt("center_z", this.centerPos.getZ());
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.mode = nbt.getInt("disco_mode");
        if (nbt.contains("center_x")) {
            this.centerPos = new BlockPos(
                    nbt.getInt("center_x"),
                    nbt.getInt("center_y"),
                    nbt.getInt("center_z")
            );
        } else {
            this.centerPos = null;
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt(nbt, registryLookup);
        return nbt;
    }
}