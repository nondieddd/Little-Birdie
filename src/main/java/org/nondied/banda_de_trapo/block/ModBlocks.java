package org.nondied.banda_de_trapo.block;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block DISCO_FLOOR = Registry.register(
            Registries.BLOCK,
            Identifier.of("banda_de_trapo", "disco_floor"),
            new DiscoFloorBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.BLACK)
                            .strength(1.5f, 6.0f)
                            .luminance(state -> 10)
            )
    );

    public static final BlockEntityType<DiscoFloorBlockEntity> DISCO_FLOOR_ENTITY_TYPE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of("banda_de_trapo", "disco_floor"),
                    FabricBlockEntityTypeBuilder.create(DiscoFloorBlockEntity::new, DISCO_FLOOR).build()
            );

    public static final Item DISCO_FLOOR_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of("banda_de_trapo", "disco_floor"),
            new BlockItem(DISCO_FLOOR, new Item.Settings())
    );

    public static final ItemGroup BANDA_DE_TRAPO_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of("banda_de_trapo", "main"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.banda_de_trapo.main"))
                    .icon(() -> new ItemStack(DISCO_FLOOR_ITEM))
                    .entries((context, entries) -> {
                        entries.add(DISCO_FLOOR_ITEM);
                    })
                    .build()
    );

    public static void register() {
    }
}