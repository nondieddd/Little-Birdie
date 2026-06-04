package org.nondied.banda_de_trapo;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.nondied.banda_de_trapo.network.ModNetworking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BandaDeTrapoManager {

    public static final BandaDeTrapoManager INSTANCE = new BandaDeTrapoManager();

    private final List<ServerPlayerEntity> activePlayers = new ArrayList<>();
    private final Set<ServerPlayerEntity> finishedPlayers = new HashSet<>();

    private int totalRounds;
    private int squaresPerRound;
    private int currentRound;
    public void startGame(Collection<ServerPlayerEntity> players, int rounds, int squares) {
        activePlayers.clear();
        finishedPlayers.clear();
        activePlayers.addAll(players);

        this.totalRounds = rounds;
        this.squaresPerRound = squares;
        this.currentRound = 1;

        for (ServerPlayerEntity player : activePlayers) {
            ServerPlayNetworking.send(player, new ModNetworking.StartGamePayload(rounds, squares));
        }
        syncProgressToAll();
    }
    public void onPlayerResult(ServerPlayerEntity player, boolean passed, int livesLeft) {
        if (!activePlayers.contains(player)) return;

        if (livesLeft <= 0) {
            activePlayers.remove(player);
        } else {
            finishedPlayers.add(player);
        }

        syncProgressToAll();
        checkRoundEnd();
    }
    public void onPlayerDisconnect(ServerPlayerEntity player) {
        if (activePlayers.remove(player)) {
            finishedPlayers.remove(player);
            syncProgressToAll();
            checkRoundEnd();
        }
    }
    private void syncProgressToAll() {
        int passedCount = finishedPlayers.size();
        int totalCount = activePlayers.size();
        ModNetworking.SyncProgressPayload payload = new ModNetworking.SyncProgressPayload(passedCount, totalCount);

        for (ServerPlayerEntity p : activePlayers) {
            ServerPlayNetworking.send(p, payload);
        }
    }
    private void checkRoundEnd() {
        if (activePlayers.isEmpty()) return;

        if (finishedPlayers.size() >= activePlayers.size()) {
            currentRound++;

            if (currentRound > totalRounds) {
                ModNetworking.EndGamePayload winPayload = new ModNetworking.EndGamePayload(true);
                for (ServerPlayerEntity p : activePlayers) {
                    ServerPlayNetworking.send(p, winPayload);
                    p.sendMessage(Text.literal("§aHas pasado de ronda."), false);
                }
                activePlayers.clear();
            } else {
                finishedPlayers.clear();
                ModNetworking.NextRoundPayload nextPayload = new ModNetworking.NextRoundPayload(currentRound);
                for (ServerPlayerEntity p : activePlayers) {
                    ServerPlayNetworking.send(p, nextPayload);
                }
                syncProgressToAll();
            }
        }
    }
}