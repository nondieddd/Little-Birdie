package org.nondied.banda_de_trapo.client.render;

import net.minecraft.util.Identifier;
import org.nondied.banda_de_trapo.entity.PeriquitoEntity;
import software.bernie.geckolib.model.GeoModel;

public class PeriquitoModel extends GeoModel<PeriquitoEntity> {

    @Override
    public Identifier getModelResource(PeriquitoEntity entity) {
        return Identifier.of("banda_de_trapo", "geo/periquito.geo.json");
    }

    @Override
    public Identifier getTextureResource(PeriquitoEntity entity) {
        return Identifier.of("banda_de_trapo", "textures/entity/periquito.png");
    }

    @Override
    public Identifier getAnimationResource(PeriquitoEntity entity) {
        return Identifier.of("banda_de_trapo", "animations/periquito.animation.json");
    }
}