package com.minecart.central_heater.item;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllDataComponents;
import com.minecart.central_heater.util.VirtualLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.common.extensions.IFluidStateExtension;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.swing.text.Style;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SturdyTankItem extends BlockItem {
    public static int MAX_FLUID_CAPACITY = 500;

    public SturdyTankItem(Properties properties) {
        super(AllBlockItem.sturdy_tank.get(), properties.stacksTo(1));
    }

    public static boolean isDrinkable(ItemStack item, Player player){
        if(FluidUtil.getFluidHandler(item).isEmpty())
            return false;
        FluidStack content = FluidUtil.getFluidHandler(item).get().getFluidInTank(0);
        if(!content.isEmpty()){
            if(content.is(Fluids.WATER)) {
                if(player.isUnderWater() &&
                        content.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)).equals(new PotionContents(Potions.WATER)))
                    return false;
                return true;
            }
        }else{
            return player.getAirSupply() != player.getMaxAirSupply();
        }
        return false;
    }

    private InteractionResultHolder<ItemStack> tryPickUpFluid(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        HitResult hitResult = this.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        }

        BlockHitResult blockHit = (BlockHitResult) hitResult;
        BlockPos pos = blockHit.getBlockPos();

        if (!level.mayInteract(player, pos)) {
            return InteractionResultHolder.pass(itemStack);
        }

        FluidState fluidState = level.getFluidState(pos);
        if (fluidState.isEmpty()) {
            return InteractionResultHolder.pass(itemStack);
        }

        var handlerOpt = FluidUtil.getFluidHandler(itemStack);
        if (handlerOpt.isEmpty()) {
            return InteractionResultHolder.pass(itemStack);
        }
        IFluidHandlerItem handler = handlerOpt.orElseThrow(null);

        int tankCapacity = handler.getTankCapacity(0);
        int currentAmount = handler.getFluidInTank(0).getAmount();
        int availableSpace = tankCapacity - currentAmount;
        int amountToFill = Math.min(1000, availableSpace);

        if (amountToFill <= 0) {
            return InteractionResultHolder.pass(itemStack);
        }

        FluidStack fluidStack = new FluidStack(fluidState.getType(), amountToFill);
        int filledAmount = handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);

        if (filledAmount > 0) {
            SoundEvent sound = fluidState.getFluidType().getSound(player, level, pos, net.neoforged.neoforge.common.SoundActions.BUCKET_FILL);
            player.playSound(sound != null ? sound : SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

            if (!level.isClientSide) {
                handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

                boolean isInfinite = fluidState.getFluidType().canConvertToSource(fluidState, level, pos);
                if (!isInfinite) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
            return InteractionResultHolder.success(handler.getContainer());
        }

        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer().isShiftKeyDown())
            return super.useOn(context);
        InteractionResultHolder<ItemStack> fluidResult = tryPickUpFluid(context.getLevel(), context.getPlayer(), context.getHand());
        context.getPlayer().setItemInHand(context.getHand(), fluidResult.getObject());
        if(fluidResult.getResult().equals(InteractionResult.PASS))
            return super.useOn(context);
        return fluidResult.getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> fluidResult = tryPickUpFluid(level, player, hand);
        if (fluidResult.getResult() == InteractionResult.SUCCESS) {
            return fluidResult;
        }

        if (isDrinkable(player.getItemInHand(hand), player)) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 48;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if (!level.isClientSide) {

            Optional<IFluidHandlerItem> optionalFLuidHandler = FluidUtil.getFluidHandler(stack);
            if(optionalFLuidHandler.isPresent()) {
                IFluidHandlerItem fluidHandler = optionalFLuidHandler.get();
                FluidStack fluidStack = fluidHandler.getFluidInTank(0);

                if (fluidStack.isEmpty()) {
                    int airSupply = player.getAirSupply();
                    int fillable = Math.min(player.getMaxAirSupply() - airSupply, 250);
                    player.setAirSupply(airSupply + fillable);
                    fluidHandler.fill(new FluidStack(Fluids.WATER, fillable * 2), IFluidHandler.FluidAction.EXECUTE);
                } else if (fluidStack.is(Fluids.WATER)) {
                    PotionContents potioncontents = fluidStack.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
                    float lengthAmplifier = fluidStack.getAmount() * 1f / 250;
                    potioncontents.forEachEffect(effect -> {
                        if (effect.getEffect().value().isInstantenous()) {
                            effect.getEffect().value().applyInstantenousEffect(player, player, entityLiving, effect.getAmplifier(), lengthAmplifier);
                        } else {
                            MobEffectInstance newEffect = new MobEffectInstance(effect.getEffect(), (int) (effect.getDuration() * lengthAmplifier), effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon());
                            entityLiving.addEffect(newEffect);
                        }
                    });
                    fluidHandler.drain(500, IFluidHandler.FluidAction.EXECUTE);
                }
                stack = fluidHandler.getContainer();
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        Optional<IFluidHandlerItem> from = FluidUtil.getFluidHandler(stack);
        Optional<IFluidHandlerItem> to = FluidUtil.getFluidHandler(slot.getItem());
        if(from.isPresent() && to.isPresent()) {
            if(!FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, false).isEmpty()) {
                FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, true);
                player.containerMenu.setCarried(from.get().getContainer());
                slot.set(to.get().getContainer());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        Optional<IFluidHandlerItem> from = FluidUtil.getFluidHandler(other);
        Optional<IFluidHandlerItem> to = FluidUtil.getFluidHandler(stack);
        if(from.isPresent() && to.isPresent()) {
            if(!FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, false).isEmpty()) {
                FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, true);
                access.set(from.get().getContainer());
                slot.set(to.get().getContainer());
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        FluidStack fluidStack = FluidUtil.getFluidHandler(stack).get().getFluidInTank(0);
        PotionContents potionContents = fluidStack.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        int color = fluidStack.getFluid().defaultFluidState().createLegacyBlock().getMapColor(VirtualLevel.getLevel(), BlockPos.ZERO).col;
        if(fluidStack.equals(Fluids.WATER))
            color = 0xFF3F76E4;
        if(fluidStack.equals(Fluids.WATER))
            color = 0xFFFF6100;
        Component fluidName = fluidStack.getHoverName();
        Component amount = Component.literal(String.valueOf(fluidStack.getAmount()));

        if(!fluidStack.isEmpty()) {
            tooltipComponents.add(CommonComponents.EMPTY);
            tooltipComponents.add(Component.translatable("container.sturdy_tank.tooltip.content_title").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("container.sturdy_tank.tooltip.content", amount, fluidName).withColor(color));
        }
        if(!potionContents.equals(new PotionContents(Potions.WATER))){
            tooltipComponents.add(Component.translatable("container.sturdy_tank.tooltip.potion_title").withStyle(ChatFormatting.GRAY));
            potionContents.addPotionTooltip(tooltipComponents::add, 1f, context.tickRate());
        }
    }
}
