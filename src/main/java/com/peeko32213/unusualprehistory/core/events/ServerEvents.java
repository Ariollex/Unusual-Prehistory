package com.peeko32213.unusualprehistory.core.events;

import com.peeko32213.unusualprehistory.UnusualPrehistory;
import com.peeko32213.unusualprehistory.common.capabilities.UPCapabilities;
import com.peeko32213.unusualprehistory.common.data.*;
import com.peeko32213.unusualprehistory.common.effect.EffectPissed;
import com.peeko32213.unusualprehistory.common.effect.EffectRabies;
import com.peeko32213.unusualprehistory.common.effect.EffectVaccineRabies;
import com.peeko32213.unusualprehistory.common.entity.EntityDunkleosteus;
import com.peeko32213.unusualprehistory.common.entity.EntityHwachavenator;
import com.peeko32213.unusualprehistory.common.entity.eggs.DinosaurLandEgg;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityBaseDinosaurAnimal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityTameableBaseDinosaurAnimal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.goal.JarateFindWaterGoal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.goal.RabiesHuntGoal;
import com.peeko32213.unusualprehistory.common.message.*;
import com.peeko32213.unusualprehistory.core.registry.UPEffects;
import com.peeko32213.unusualprehistory.core.registry.UPItems;
import com.peeko32213.unusualprehistory.core.registry.UPMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = UnusualPrehistory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {


    @SubscribeEvent
    public static void onRegisterReloadListeners(ServerStartedEvent event) {
        try{
            AnalyzerRecipeJsonManager.populateRecipeMap(event.getServer().getLevel(Level.OVERWORLD));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @SubscribeEvent
    public static void sendMapPackets(PlayerEvent.PlayerLoggedInEvent event){

    }
    @SubscribeEvent
    public static void synchDataPack(OnDatapackSyncEvent event){
        ServerPlayer player = event.getPlayer();
        List<ServerPlayer> playerList = event.getPlayerList().getPlayers();
        Map<Integer, List<LootFruitCodec>> lootFruitsTier = LootFruitJsonManager.getTierTrades();
        Map<Item, List<LootFruitCodec>> lootFruits = LootFruitJsonManager.getTrades();
        Map<Item, List<ItemWeightedPairCodec>> analyzerRecipes = AnalyzerRecipeJsonManager.getRecipes();
        Map<ResourceLocation, EncyclopediaCodec> encyclopediaEntries = EncyclopediaJsonManager.getEncyclopediaEntries();
        EncyclopediaCodec rootPage = EncyclopediaJsonManager.getRootPage();
       if(player != null){
           UPMessages.sendToPlayer(new LootFruitTierPacketS2C(lootFruitsTier), player);
           UPMessages.sendToPlayer(new LootFruitPacketS2C(lootFruits), player);
           UPMessages.sendToPlayer(new AnalyzerRecipeS2C(analyzerRecipes), player);
           UPMessages.sendToPlayer(new EncyclopediaS2C(encyclopediaEntries), player);
           UPMessages.sendToPlayer(new EncyclopediaRootPageS2C(rootPage), player);
       }


       if(playerList != null && !playerList.isEmpty()){
           for(ServerPlayer player1 : playerList){
               ServerLevel serverLevel = (ServerLevel) player1.level();
               if(AnalyzerRecipeJsonManager.getRecipes().isEmpty()){
                   AnalyzerRecipeJsonManager.populateRecipeMap(serverLevel);
               }
               Map<Item, List<ItemWeightedPairCodec>> analyzerRecipesReload = AnalyzerRecipeJsonManager.getRecipes();
               UPMessages.sendToPlayer(new LootFruitTierPacketS2C(lootFruitsTier), player1);
               UPMessages.sendToPlayer(new LootFruitPacketS2C(lootFruits), player1);
               UPMessages.sendToPlayer(new AnalyzerRecipeS2C(analyzerRecipesReload), player1);
               UPMessages.sendToPlayer(new EncyclopediaS2C(encyclopediaEntries), player1);
               UPMessages.sendToPlayer(new EncyclopediaRootPageS2C(rootPage), player1);
           }
       }
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new AnalyzerRecipeJsonManager());
        event.addListener(new LootFruitJsonManager());
        event.addListener(new EncyclopediaJsonManager());
    }


    @SubscribeEvent
    public void onLootLevelEvent(LootingLevelEvent event) {
        DamageSource src = event.getDamageSource();
        if (src != null) {
            if (src.getEntity() instanceof EntityDunkleosteus) {
                event.setLootingLevel(event.getLootingLevel() + 3);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!event.getEntity().getUseItem().isEmpty() && event.getSource() != null && event.getSource().getEntity() != null) {
            if (event.getEntity().getUseItem().getItem() == UPItems.TRIKE_SHIELD.get()) {
                if (event.getSource().getEntity() instanceof LivingEntity living && !living.isFallFlying()) {
                    boolean flag = false;
                    if (living.distanceTo(event.getEntity()) <= 4
                            && !living.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1));
                        flag = true;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingDamageEvent event) {
            if (event.getSource().getEntity() instanceof LivingEntity living) {

                LivingEntity entity = event.getEntity();
                Entity offender = event.getSource().getEntity();

                if (offender instanceof LivingEntity && ((LivingEntity) offender).hasEffect(UPEffects.RABIES.get())) {
                    //rabies spread
                    entity.addEffect(new MobEffectInstance(UPEffects.RABIES.get(), -1));
                    event.setAmount(event.getAmount()/4);
                    //rabid animals only deal quarter damage to each other
                }

                if (entity.hasEffect(UPEffects.PISSED_UPON.get())) {
                    //urine triples damage
                    event.setAmount(event.getAmount() * 3);
                }

                ItemStack itemStack = living.getItemInHand(InteractionHand.MAIN_HAND);
                if(!itemStack.hasTag()) return;
                CompoundTag tag = itemStack.getTag();
                if(tag == null || !tag.contains("megalania_damage")) return;;
                int count = tag.getInt("megalania_damage");
                float amount = event.getAmount();
                amount *= 0.75;
                int hpReduction = 0;

                if(entity.hasEffect(UPEffects.HEALTH_REDUCTION.get())){
                    hpReduction = entity.getEffect(UPEffects.HEALTH_REDUCTION.get()).getAmplifier() + 1;
                }

                MobEffectInstance mobEffectInstance = new MobEffectInstance(UPEffects.HEALTH_REDUCTION.get(), 120, hpReduction);
                entity.addEffect(mobEffectInstance);
                event.setAmount(amount);
                count--;
                tag.putInt("megalania_damage", count);
                if(count <= 0) tag.remove("megalania_damage");
                itemStack.setTag(tag);
            }
    }

    @SubscribeEvent
    //cant be canceled
    public void jarateFacilitatorEvent(LivingEvent.LivingTickEvent event) {
        Entity titty = event.getEntity();

        if(titty instanceof PathfinderMob && ((PathfinderMob) titty).hasEffect(UPEffects.PISSED_UPON.get()) && !checkContainPiss(((PathfinderMob) titty).goalSelector.getAvailableGoals())) {
            //has effect but has no piss(add)
            ((PathfinderMob) titty).goalSelector.addGoal(-1, new JarateFindWaterGoal(((PathfinderMob) titty)));
        }

        if(titty instanceof PathfinderMob && !((PathfinderMob) titty).hasEffect(UPEffects.PISSED_UPON.get()) && checkContainPiss(((PathfinderMob) titty).goalSelector.getAvailableGoals())) {
            //has no effect but has piss(remove)
            cutPissGoal(((PathfinderMob) titty));
        }
    }

    @SubscribeEvent
    //cant be canceled
    public void rabiesFacilitatorEvent(LivingEvent.LivingTickEvent event) {
        Entity titty = event.getEntity();

        if(titty instanceof PathfinderMob && ((PathfinderMob) titty).hasEffect(UPEffects.RABIES.get()) && !checkContainRabies(((PathfinderMob) titty).goalSelector.getAvailableGoals())) {
            //has effect but has no piss(add)
            ((PathfinderMob) titty).goalSelector.addGoal(-1, new RabiesHuntGoal(((PathfinderMob) titty)));
            //TODO: Make rabies only manifest after a certain number of ticks
        }

        if(titty instanceof PathfinderMob && !((PathfinderMob) titty).hasEffect(UPEffects.RABIES.get()) && checkContainRabies(((PathfinderMob) titty).goalSelector.getAvailableGoals())) {
            //has no effect but has piss(remove)
            cutRabies(((PathfinderMob) titty));
        }
    }

    @SubscribeEvent
    public void thingsThatCannotBeMilkedEvent(MobEffectEvent.Remove event) {
        if (event.getEffect() instanceof EffectRabies && !event.getEntity().hasEffect(UPEffects.RABIES_VACCINE.get())) {
            //rabies can't be milked away unless you are vaccinated
            event.setCanceled(true);
        }

        if (event.getEffect() instanceof EffectVaccineRabies) {

            if(event.getEntity() instanceof ServerPlayer serverPlayer){
                serverPlayer.getCapability(UPCapabilities.PLAYER_CAPABILITY).ifPresent(capability -> {

                    if (serverPlayer.hasEffect(UPEffects.RABIES_VACCINE.get())) {
                        capability.playerVaccinationTime = 0;
                        //System.out.println(capability.playerVaccinationTime);
                    }
                    //set vac time to 0 if the player has no vacc effect

                });

            }
        } else {
            event.getEntity().getCapability(UPCapabilities.ANIMAL_CAPABILITY).ifPresent(capability -> {
                if (!event.getEntity().hasEffect(UPEffects.RABIES_VACCINE.get())) {
                    capability.entityVaccinationTime = 0;
                    //set vac time to 0 if the entity has no vacc effect
                }
            });
        }
        //can remove vaccines so that particles don't shit themselves
    }

    private boolean checkContainPiss(Set<WrappedGoal> availableGoals) {
        WrappedGoal[] arring = availableGoals.toArray(new WrappedGoal[0]);

        for (int i = 0; i < arring.length; i++) {
            if (arring[i].getGoal() instanceof JarateFindWaterGoal) {
                return true;
                //has piss goal
            }
        }

        return false;
        //has no piss goal
        //remember this check also happens clientside and if that's the case it returns false.
    }

    private boolean checkContainRabies(Set<WrappedGoal> availableGoals) {
        WrappedGoal[] arring = availableGoals.toArray(new WrappedGoal[0]);

        for (int i = 0; i < arring.length; i++) {
            if (arring[i].getGoal() instanceof RabiesHuntGoal) {
                return true;
                //has piss goal
            }
        }

        return false;
        //has no piss goal
        //remember this check also happens clientside and if that's the case it returns false.
    }

    private void cutPissGoal(PathfinderMob titty) {
        WrappedGoal[] arring = titty.goalSelector.getAvailableGoals().toArray(new WrappedGoal[0]);

        for (int i = 0; i < arring.length; i++) {
            if (arring[i].getGoal() instanceof JarateFindWaterGoal) {
                titty.goalSelector.removeGoal(arring[i].getGoal());
            }
        }
    }

    private void cutRabies(PathfinderMob titty) {
        WrappedGoal[] arring = titty.goalSelector.getAvailableGoals().toArray(new WrappedGoal[0]);

        for (int i = 0; i < arring.length; i++) {
            if (arring[i].getGoal() instanceof RabiesHuntGoal) {
                titty.goalSelector.removeGoal(arring[i].getGoal());
            }
        }
    }

    @SubscribeEvent
    public static void renderMegalaniaPoisonToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(!stack.hasTag()) return;
        CompoundTag tag = stack.getTag();
        if(tag == null || !tag.contains("megalania_damage")) return;
        int amount = tag.getInt("megalania_damage");
        MutableComponent component = Component.translatable("unusualprehistory.megalania_damage", amount).withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.ITALIC);
        List<Component> toolTip = event.getToolTip();
        event.getToolTip().add(component);
    }


    @SubscribeEvent
    //cant be canceled
    public void preventClick(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof EntityDunkleosteus dunkleosteus) {
            dunkleosteus.killed();
        }

        if (event.getSource().getEntity() instanceof EntityBaseDinosaurAnimal dinosaurAnimal) {
            dinosaurAnimal.killed();
        }

        if (event.getSource().getEntity() instanceof EntityTameableBaseDinosaurAnimal dinosaurAnimal) {
            dinosaurAnimal.killed();
        }

        if (event.getSource().getEntity() instanceof EntityHwachavenator dinosaurAnimal) {
            dinosaurAnimal.killed();
        }
    }


    @SubscribeEvent
    //cant be canceled
    public void axeOneHitWoodDestroy(PlayerInteractEvent.LeftClickBlock event) {
        if(event.getEntity() != null && !event.getLevel().isClientSide){
            Player player = event.getEntity();
            ServerLevel serverLevel = (ServerLevel) event.getLevel();
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!itemStack.is(UPItems.HANDMADE_BATTLEAXE.get())) return;
            BlockPos pos = event.getPos();
            BlockState state = serverLevel.getBlockState(pos);
            RandomSource randomSource = serverLevel.random;
            boolean giveDrops = randomSource.nextInt(100) < 10;
            if(state.is(BlockTags.MINEABLE_WITH_AXE))
            {
                if(!giveDrops){
                    serverLevel.destroyBlock(pos, false);
                } else {
                    serverLevel.destroyBlock(pos, true);
                }

            }
        }
    }

    @SubscribeEvent
    public void clubExtraDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living) {
            ItemStack itemStack = living.getItemInHand(InteractionHand.MAIN_HAND);
            if(!itemStack.is(UPItems.HANDMADE_CLUB.get())) return;
            float extraDamageMultp = 1- living.getArmorCoverPercentage();
            float damage = event.getAmount();
            damage *= extraDamageMultp;
            event.setAmount(damage);
        }
    }


    @SubscribeEvent
    //cant be canceled
    public void spearThrust(PlayerInteractEvent.LeftClickEmpty event) {
        if(event.getEntity() != null ){
            Player player = event.getEntity();
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!itemStack.is(UPItems.HANDMADE_SPEAR.get()) || player.isFallFlying()) return;
            if(player.getCooldowns().isOnCooldown(itemStack.getItem())) return;
            Vec3 vec3 = player.getDeltaMovement();
            Vec3 vec32 = new Vec3(3,1,3).multiply(player.getLookAngle());
            player.setDeltaMovement(vec32);
            player.hurtMarked = true;
            //player.invulnerableTime = 10;
            player.getCooldowns().addCooldown(itemStack.getItem(), 60);
        }
    }

    @SubscribeEvent
    //cant be canceled
    public void spearFallDamageAttack(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START || event.player.level().isClientSide) return;
        if(event.player instanceof ServerPlayer serverPlayer){
            ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
            ItemStack itemStack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            if(!itemStack.is(UPItems.HANDMADE_SPEAR.get()) || (serverPlayer.fallDistance < 4)) return;
            if(serverPlayer.getCooldowns().isOnCooldown(itemStack.getItem())) return;
            BlockPos blockPos = serverPlayer.getOnPos();
            if(!serverLevel.getBlockState(blockPos).isAir()){
                RandomSource randomSource = serverLevel.random;
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 50, randomSource.nextGaussian() * 1, 0.1, randomSource.nextGaussian() * 1, 0.2 );

                List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, serverPlayer.getBoundingBox().inflate(4), (e) -> !(e.is(serverPlayer)));
                for(LivingEntity entity : entities)
                {
                    entity.hurt(serverPlayer.damageSources().mobAttack(serverPlayer), serverPlayer.fallDistance);
                }
            }

        }
    }

    //@SubscribeEvent
    //public void removeCachedBlock(BlockEvent.BreakEvent event) {
    //    if(DinosaurLandEgg.cachedPos.containsKey(event.getPos())) {
    //        DinosaurLandEgg.cachedPos.remove(event.getPos());
    //    }
    //}

    @SubscribeEvent
    //cant be canceled
    public void preventClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void preventClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void preventClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    //cant be canceled
    public void preventClick(PlayerInteractEvent.RightClickEmpty event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void preventClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void preventInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void preventDamage(AttackEntityEvent event) {
        if (event.getEntity().hasEffect(UPEffects.PREVENT_CLICK.get())) {
            event.setCanceled(true);
        }
    }
}





