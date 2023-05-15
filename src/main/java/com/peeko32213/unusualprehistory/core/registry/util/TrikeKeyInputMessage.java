package com.peeko32213.unusualprehistory.core.registry.util;

import com.peeko32213.unusualprehistory.common.entity.EntityTriceratops;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class TrikeKeyInputMessage {
    public int key;
    public static final Logger LOGGER = LogManager.getLogger();
    public TrikeKeyInputMessage(int key) {
        this.key = key;
    }

    public TrikeKeyInputMessage(FriendlyByteBuf buf) {
        this.key = buf.readInt();
    }


    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(key);
    }

    public static void handle(TrikeKeyInputMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            Entity vehicle = player.getVehicle();


            if (vehicle instanceof EntityTriceratops triceratops && triceratops.getControllingPassenger() == player) {
                if(!(triceratops.attackCooldown > 0)) {
                    triceratops.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
                    triceratops.setSwinging(true);
                    triceratops.performAttack();
                    triceratops.attackCooldown = EntityTriceratops.ATTACK_COOLDOWN;
                    triceratops.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23F);
                }



                    //Player rider = (Player)triceratops.getControllingPassenger();
                    //if(rider.getLastHurtMob() != null && triceratops.distanceTo(rider.getLastHurtMob()) < triceratops.getBbWidth() + 3F && !triceratops.isAlliedTo(rider.getLastHurtMob())){
                    //    UUID preyUUID = rider.getLastHurtMob().getUUID();
                    //    if (!triceratops.getUUID().equals(preyUUID) && triceratops.riderAttackCooldown == 0) {
                    //        triceratops.doHurtTarget(rider.getLastHurtMob());
                    //        triceratops.setSwinging(true);
                    //        triceratops.riderAttackCooldown = 20;
                    //    }
                    //}
                }

        });
        context.setPacketHandled(true);
    }
}
