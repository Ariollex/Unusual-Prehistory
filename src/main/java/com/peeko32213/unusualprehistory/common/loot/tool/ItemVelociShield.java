package com.peeko32213.unusualprehistory.common.loot.tool;

import com.peeko32213.unusualprehistory.client.model.tool.VelociShieldModel;
import com.peeko32213.unusualprehistory.client.render.tool.ToolRenderer;
import com.peeko32213.unusualprehistory.core.registry.UPItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ItemVelociShield extends ShieldItem  implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public ItemVelociShield(Properties group) {
        super(group);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }


    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.BLOCK;
    }


    @Override
    public int getEnchantmentValue() {
        return 1;
    }


    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player livingEntityIn, InteractionHand hand) {
        ItemStack itemstack = livingEntityIn.getItemInHand(hand);
        Vec3 view = livingEntityIn.getViewVector(1.0F);

        livingEntityIn.setDeltaMovement(view.multiply(2.0D, 1.0D, 2.0D));
        livingEntityIn.getCooldowns().addCooldown(this, 50);

        itemstack.hurtAndBreak(1, livingEntityIn, (player) -> {
            player.broadcastBreakEvent(livingEntityIn.getUsedItemHand());
        });
        return super.use(level, livingEntityIn, hand);
    }


    public boolean isValidRepairItem(ItemStack p_82789_1_, ItemStack p_82789_2_) {
        return UPItems.AMBER_SHARDS.get() == p_82789_2_.getItem() || super.isValidRepairItem(p_82789_1_, p_82789_2_);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final ToolRenderer renderer = new ToolRenderer<>(new VelociShieldModel());

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", state -> PlayState.CONTINUE)
                .triggerableAnim("idle", DefaultAnimations.IDLE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}