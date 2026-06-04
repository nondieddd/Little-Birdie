package org.nondied.banda_de_trapo.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.nondied.banda_de_trapo.block.ModBlocks;
import org.nondied.banda_de_trapo.client.gui.BandaDeTrapoScreen;
import org.nondied.banda_de_trapo.client.render.DiscoFloorRenderer;
import org.nondied.banda_de_trapo.client.render.PeriquitoRenderer;
import org.nondied.banda_de_trapo.entity.ModEntities;
import org.nondied.banda_de_trapo.client.render.PlayerArrowRenderer;
import org.nondied.banda_de_trapo.network.ModNetworking;

public class Banda_de_trapoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PlayerArrowRenderer.register();
        EntityRendererRegistry.register(ModEntities.PERIQUITO, PeriquitoRenderer::new);
        BlockEntityRendererFactories.register(ModBlocks.DISCO_FLOOR_ENTITY_TYPE, DiscoFloorRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.StartGamePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                context.client().setScreen(new BandaDeTrapoScreen(payload.rounds(), payload.squares()));
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.SyncProgressPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().currentScreen instanceof BandaDeTrapoScreen screen) {
                    screen.updateProgress(payload.passed(), payload.total());
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.NextRoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().currentScreen instanceof BandaDeTrapoScreen screen) {
                    screen.startNextRound(payload.roundNumber());
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.EndGamePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().currentScreen instanceof BandaDeTrapoScreen screen) {
                    screen.forceEndGame(payload.won());
                }
            });
        });
    }
}