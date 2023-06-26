package com.peeko32213.unusualprehistory.common.capabilities;

import com.peeko32213.unusualprehistory.UnusualPrehistory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

public class UPAnimalAttacherCapability {

    private static class UPCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(UnusualPrehistory.MODID, "animal_capability");
        private final UPAnimalCapability backend = new UPAnimalCapability();
        private final LazyOptional<UPAnimalCapability> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return UPCapabilities.ANIMAL_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }

    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Animal) {
            final UPAnimalAttacherCapability.UPCapabilityProvider provider = new UPAnimalAttacherCapability.UPCapabilityProvider();
            event.addCapability(UPAnimalAttacherCapability.UPCapabilityProvider.IDENTIFIER, provider);
        }
    }

}
