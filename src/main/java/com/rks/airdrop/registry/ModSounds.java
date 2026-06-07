package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RksAirdrops.MODID);

    public static final RegistryObject<SoundEvent> AIRPLANE_SOUND_1 = register("airplane_sound_1");
    public static final RegistryObject<SoundEvent> AIRPLANE_SOUND_2 = register("airplane_sound_2");
    public static final RegistryObject<SoundEvent> AIRPLANE_SOUND_3 = register("airplane_sound_3");

    private static final List<RegistryObject<SoundEvent>> AIRPLANE_SOUNDS = List.of(
            AIRPLANE_SOUND_1,
            AIRPLANE_SOUND_2,
            AIRPLANE_SOUND_3
    );

    private ModSounds() {
    }

    public static SoundEvent randomAirplaneSound(RandomSource random) {
        return AIRPLANE_SOUNDS.get(random.nextInt(AIRPLANE_SOUNDS.size())).get();
    }

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                new ResourceLocation(RksAirdrops.MODID, name)
        ));
    }
}
