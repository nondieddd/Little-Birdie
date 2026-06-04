package org.nondied.banda_de_trapo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.nondied.banda_de_trapo.block.ModBlocks;
import org.nondied.banda_de_trapo.command.BandaDeTrapoCommand;
import org.nondied.banda_de_trapo.entity.ModEntities;
import org.nondied.banda_de_trapo.entity.PeriquitoEntity;
import org.nondied.banda_de_trapo.network.ModNetworking;
import org.nondied.banda_de_trapo.sound.ModSounds;
import software.bernie.geckolib.GeckoLib;

public class Banda_de_trapo implements ModInitializer {

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModEntities.registerModEntities();
        FabricDefaultAttributeRegistry.register(
                ModEntities.PERIQUITO,
                PeriquitoEntity.createAttributes()
        );

        ModSounds.registerModSounds();
        PayloadTypeRegistry.playS2C().register(ModNetworking.StartGamePayload.ID, ModNetworking.StartGamePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ModNetworking.SyncProgressPayload.ID, ModNetworking.SyncProgressPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ModNetworking.NextRoundPayload.ID, ModNetworking.NextRoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ModNetworking.EndGamePayload.ID, ModNetworking.EndGamePayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ModNetworking.RoundResultPayload.ID, ModNetworking.RoundResultPayload.CODEC);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BandaDeTrapoCommand.register(dispatcher);
        });
        ServerPlayNetworking.registerGlobalReceiver(ModNetworking.RoundResultPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                if (!payload.passed() && payload.livesLeft() <= 0) {
                    context.player().kill();
                }
                BandaDeTrapoManager.INSTANCE.onPlayerResult(context.player(), payload.passed(), payload.livesLeft());
            });
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            BandaDeTrapoManager.INSTANCE.onPlayerDisconnect(handler.player);
        });
    }
}