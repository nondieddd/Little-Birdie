package org.nondied.banda_de_trapo.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class ModNetworking {

    public record StartGamePayload(int rounds, int squares) implements CustomPayload {
        public static final CustomPayload.Id<StartGamePayload> ID = new CustomPayload.Id<>(Identifier.of("banda_de_trapo", "start_game"));
        public static final PacketCodec<PacketByteBuf, StartGamePayload> CODEC = PacketCodec.of(
                (value, buf) -> { buf.writeInt(value.rounds()); buf.writeInt(value.squares()); },
                buf -> new StartGamePayload(buf.readInt(), buf.readInt())
        );
        @Override public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    public record SyncProgressPayload(int passed, int total) implements CustomPayload {
        public static final CustomPayload.Id<SyncProgressPayload> ID = new CustomPayload.Id<>(Identifier.of("banda_de_trapo", "sync_progress"));
        public static final PacketCodec<PacketByteBuf, SyncProgressPayload> CODEC = PacketCodec.of(
                (value, buf) -> { buf.writeInt(value.passed()); buf.writeInt(value.total()); },
                buf -> new SyncProgressPayload(buf.readInt(), buf.readInt())
        );
        @Override public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    public record NextRoundPayload(int roundNumber) implements CustomPayload {
        public static final CustomPayload.Id<NextRoundPayload> ID = new CustomPayload.Id<>(Identifier.of("banda_de_trapo", "next_round"));
        public static final PacketCodec<PacketByteBuf, NextRoundPayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeInt(value.roundNumber()),
                buf -> new NextRoundPayload(buf.readInt())
        );
        @Override public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    public record EndGamePayload(boolean won) implements CustomPayload {
        public static final CustomPayload.Id<EndGamePayload> ID = new CustomPayload.Id<>(Identifier.of("banda_de_trapo", "end_game"));
        public static final PacketCodec<PacketByteBuf, EndGamePayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeBoolean(value.won()),
                buf -> new EndGamePayload(buf.readBoolean())
        );
        @Override public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    public record RoundResultPayload(boolean passed, int livesLeft) implements CustomPayload {
        public static final CustomPayload.Id<RoundResultPayload> ID = new CustomPayload.Id<>(Identifier.of("banda_de_trapo", "round_result"));
        public static final PacketCodec<PacketByteBuf, RoundResultPayload> CODEC = PacketCodec.of(
                (value, buf) -> { buf.writeBoolean(value.passed()); buf.writeInt(value.livesLeft()); },
                buf -> new RoundResultPayload(buf.readBoolean(), buf.readInt())
        );
        @Override public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }
}