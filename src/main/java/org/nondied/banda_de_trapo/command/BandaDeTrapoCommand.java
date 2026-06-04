package org.nondied.banda_de_trapo.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.nondied.banda_de_trapo.BandaDeTrapoManager;
import org.nondied.banda_de_trapo.block.DiscoFloorBlock;
import org.nondied.banda_de_trapo.block.DiscoFloorBlockEntity;
import org.nondied.banda_de_trapo.block.ModBlocks;
import org.nondied.banda_de_trapo.entity.ModEntities;
import org.nondied.banda_de_trapo.entity.PeriquitoEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class BandaDeTrapoCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("periquito")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("iniciar")
                                .then(CommandManager.argument("jugadores", EntityArgumentType.players())
                                        .then(CommandManager.argument("rondas", IntegerArgumentType.integer(1, 10))
                                                .then(CommandManager.argument("cuadros", IntegerArgumentType.integer(1, 7))
                                                        .executes(context -> {
                                                            Collection<ServerPlayerEntity> targets =
                                                                    EntityArgumentType.getPlayers(context, "jugadores");
                                                            int rondas = IntegerArgumentType.getInteger(context, "rondas");
                                                            int cuadros = IntegerArgumentType.getInteger(context, "cuadros");
                                                            BandaDeTrapoManager.INSTANCE.startGame(targets, rondas, cuadros);
                                                            context.getSource().sendFeedback(() ->
                                                                    Text.literal("Minijuego iniciado para " + targets.size() + " jugador(es)"), true);
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )

                        .then(CommandManager.literal("piso")
                                .then(CommandManager.argument("modo", IntegerArgumentType.integer(1, 10))
                                        .executes(context -> {
                                            int modo = IntegerArgumentType.getInteger(context, "modo");
                                            ServerCommandSource source = context.getSource();
                                            BlockPos playerPos = BlockPos.ofFloored(source.getPosition());
                                            World world = source.getWorld();
                                            BlockPos targetPos = playerPos;
                                            while (targetPos.getY() > world.getBottomY()
                                                    && !world.getBlockState(targetPos).isOf(ModBlocks.DISCO_FLOOR)) {
                                                targetPos = targetPos.down();
                                            }
                                            if (world.getBlockEntity(targetPos) instanceof DiscoFloorBlockEntity be) {
                                                cambiarModoPista(world, targetPos, modo);
                                                context.getSource().sendFeedback(() ->
                                                        Text.literal("Modo de pista cambiado a " + modo), false);
                                            } else {
                                                context.getSource().sendFeedback(() ->
                                                        Text.literal("No se encontró piso de discoteca debajo tuyo"), false);
                                            }
                                            return 1;
                                        })
                                )
                        )

                        .then(CommandManager.literal("añadir")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    if (player == null) return 0;
                                    HitResult hitResult = player.raycast(10.0D, 1.0F, false);
                                    return spawnPeriquito(source, player, hitResult.getPos());
                                })
                                .then(CommandManager.argument("pos", Vec3ArgumentType.vec3())
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            ServerPlayerEntity player = source.getPlayer();
                                            Vec3d pos = Vec3ArgumentType.getVec3(context, "pos");
                                            return spawnPeriquito(source, player, pos);
                                        })
                                )
                        )

                        .then(CommandManager.literal("eliminar")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    World world = source.getWorld();
                                    Vec3d pos = source.getPosition();

                                    List<PeriquitoEntity> periquitos = world.getEntitiesByClass(
                                            PeriquitoEntity.class,
                                            new Box(pos.x - 100, pos.y - 100, pos.z - 100,
                                                    pos.x + 100, pos.y + 100, pos.z + 100),
                                            entity -> true
                                    );

                                    if (periquitos.isEmpty()) {
                                        source.sendFeedback(() ->
                                                Text.literal("No hay ningún Periquito."), false);
                                        return 0;
                                    }

                                    int count = periquitos.size();
                                    periquitos.forEach(net.minecraft.entity.Entity::discard);
                                    source.sendFeedback(() ->
                                            Text.literal("Se eliminaron " + count + " Periquito(s)."), true);
                                    return count;
                                })
                        )

                        .then(CommandManager.literal("recalcular")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    World world = source.getWorld();
                                    BlockPos start = BlockPos.ofFloored(source.getPosition());

                                    while (start.getY() > world.getBottomY()
                                            && !world.getBlockState(start).isOf(ModBlocks.DISCO_FLOOR)) {
                                        start = start.down();
                                    }

                                    if (world.getBlockState(start).isOf(ModBlocks.DISCO_FLOOR)) {
                                        ((DiscoFloorBlock) ModBlocks.DISCO_FLOOR).recalcularCentro(world, start);
                                        source.sendFeedback(() ->
                                                Text.literal("Centro recalculado correctamente."), false);
                                    } else {
                                        source.sendFeedback(() ->
                                                Text.literal("No hay piso de discoteca debajo tuyo."), false);
                                    }
                                    return 1;
                                })
                        )
        );
    }

    private static int spawnPeriquito(ServerCommandSource source, ServerPlayerEntity player, Vec3d pos) {
        PeriquitoEntity periquito = ModEntities.PERIQUITO.create(source.getWorld());
        if (periquito != null) {
            float yaw = (player != null) ? player.getYaw() : 0.0f;
            float pitch = (player != null) ? player.getPitch() : 0.0f;

            periquito.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, pitch);
            periquito.setHeadYaw(yaw);
            periquito.setBodyYaw(yaw);

            source.getWorld().spawnEntity(periquito);
            source.sendFeedback(() -> Text.literal(
                    String.format("Periquito añadido en [%.1f, %.1f, %.1f]", pos.x, pos.y, pos.z)
            ), false);
        }
        return 1;
    }

    private static void cambiarModoPista(World world, BlockPos startPos, int modo) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (world.getBlockEntity(current) instanceof DiscoFloorBlockEntity be) {
                be.setMode(modo);
            }
            for (BlockPos offset : new BlockPos[]{
                    current.north(), current.south(),
                    current.east(),  current.west()}) {
                if (!visited.contains(offset) && world.getBlockState(offset).isOf(ModBlocks.DISCO_FLOOR)) {
                    visited.add(offset);
                    queue.add(offset);
                }
            }
        }
    }
}