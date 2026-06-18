package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, RksAirdrops.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> AIRPLANE_SOUND_1 = register("airplane_sound_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> AIRPLANE_SOUND_2 = register("airplane_sound_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> AIRPLANE_SOUND_3 = register("airplane_sound_3");

    private static final List<DeferredHolder<SoundEvent, SoundEvent>> AIRPLANE_SOUNDS = List.of(
            AIRPLANE_SOUND_1,
            AIRPLANE_SOUND_2,
            AIRPLANE_SOUND_3
    );

    private ModSounds() {
    }

    public static SoundEvent randomAirplaneSound(RandomSource random) {
        return AIRPLANE_SOUNDS.get(random.nextInt(AIRPLANE_SOUNDS.size())).get();
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(RksAirdrops.MODID, name)
        ));
    }
}



