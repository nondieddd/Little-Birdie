package org.nondied.banda_de_trapo.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static SoundEvent ANUNCIAR_NUMERO;
    public static SoundEvent COLOCAR_NUMERO;
    public static SoundEvent CORRECT;
    public static SoundEvent IDLE;
    public static SoundEvent LOSE;
    public static SoundEvent NUMERO_1;
    public static SoundEvent NUMERO_2;
    public static SoundEvent NUMERO_3;
    public static SoundEvent NUMERO_4;
    public static SoundEvent NUMERO_5;
    public static SoundEvent NUMERO_6;
    public static SoundEvent NUMERO_7;
    public static SoundEvent NUMERO_8;
    public static SoundEvent NUMERO_9;
    public static SoundEvent NUMERO_10;
    public static SoundEvent NUMERO_11;
    public static SoundEvent PASS;
    public static SoundEvent PERIQUITO;
    public static SoundEvent SFX1;
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of("banda_de_trapo", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void registerModSounds() {
        ANUNCIAR_NUMERO = registerSoundEvent("sonido.anunciarnumero");
        COLOCAR_NUMERO = registerSoundEvent("sonido.colocarnumero");
        CORRECT = registerSoundEvent("sonido.correct");
        IDLE = registerSoundEvent("sonido.idle");
        LOSE = registerSoundEvent("sonido.lose");
        NUMERO_1 = registerSoundEvent("sonido.numero_1");
        NUMERO_2 = registerSoundEvent("sonido.numero_2");
        NUMERO_3 = registerSoundEvent("sonido.numero_3");
        NUMERO_4 = registerSoundEvent("sonido.numero_4");
        NUMERO_5 = registerSoundEvent("sonido.numero_5");
        NUMERO_6 = registerSoundEvent("sonido.numero_6");
        NUMERO_7 = registerSoundEvent("sonido.numero_7");
        NUMERO_8 = registerSoundEvent("sonido.numero_8");
        NUMERO_9 = registerSoundEvent("sonido.numero_9");
        NUMERO_10 = registerSoundEvent("sonido.numero_10");
        NUMERO_11 = registerSoundEvent("sonido.numero_11");
        PASS = registerSoundEvent("sonido.pass");
        PERIQUITO = registerSoundEvent("sonido.periquitoperiquito");
        SFX1 = registerSoundEvent("sonido.sfx1");

        System.out.println("Sonidos registrados correctamente.");
    }
}