package org.nondied.banda_de_trapo.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<PeriquitoEntity> PERIQUITO = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("banda_de_trapo", "periquito"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PeriquitoEntity::new)
                    .dimensions(EntityDimensions.fixed(1.6f, 3.6f))
                    .build()
    );

    public static void registerModEntities() {
    }
}