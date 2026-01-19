package com.minecart.central_heater.item.complex_items;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.misc.VirtualLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SturdyTankItem extends BlockItem {
    public static int MAX_FLUID_CAPACITY = 500;

    public SturdyTankItem(Properties properties) {
        super(AllBlockItem.STURDY_TANK.get(), properties.stacksTo(1));
    }

    // Forge 1.20.1: Must attach capability to store fluid on the item
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, MAX_FLUID_CAPACITY);
    }

    public static boolean isDrinkable(ItemStack item, Player player) {
        Optional<IFluidHandlerItem> handlerOpt = FluidUtil.getFluidHandler(item).resolve();
        if (handlerOpt.isEmpty())
            return false;

        FluidStack content = handlerOpt.get().getFluidInTank(0);
        if (!content.isEmpty()) {
            if (content.getFluid() == Fluids.WATER) {
                // Check for Potion NBT on the FluidStack
                Potion potion = PotionUtils.getPotion(content.getTag());
                if (player.isUnderWater() && potion == Potions.WATER)
                    return false;
                return true;
            }
        } else {
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

        Optional<IFluidHandlerItem> handlerOpt = FluidUtil.getFluidHandler(itemStack).resolve();
        if (handlerOpt.isEmpty()) {
            return InteractionResultHolder.pass(itemStack);
        }
        IFluidHandlerItem handler = handlerOpt.get();

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
            SoundEvent sound = fluidState.getFluidType().getSound(player, level, pos, SoundActions.BUCKET_FILL);
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
        if (context.getPlayer().isShiftKeyDown())
            return super.useOn(context);
        InteractionResultHolder<ItemStack> fluidResult = tryPickUpFluid(context.getLevel(), context.getPlayer(), context.getHand());
        context.getPlayer().setItemInHand(context.getHand(), fluidResult.getObject());
        if (fluidResult.getResult() == InteractionResult.PASS)
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
    public int getUseDuration(ItemStack stack) {
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
        Player player = entityLiving instanceof Player ? (Player) entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if (!level.isClientSide) {
            Optional<IFluidHandlerItem> optionalFLuidHandler = FluidUtil.getFluidHandler(stack).resolve();
            if (optionalFLuidHandler.isPresent()) {
                IFluidHandlerItem fluidHandler = optionalFLuidHandler.get();
                FluidStack fluidStack = fluidHandler.getFluidInTank(0);

                if (fluidStack.isEmpty()) {
                    if (player != null) {
                        int airSupply = player.getAirSupply();
                        int fillable = Math.min(player.getMaxAirSupply() - airSupply, 250);
                        player.setAirSupply(airSupply + fillable);
                        fluidHandler.fill(new FluidStack(Fluids.WATER, fillable * 2), IFluidHandler.FluidAction.EXECUTE);
                    }
                } else if (fluidStack.getFluid() == Fluids.WATER) {
                    float lengthAmplifier = fluidStack.getAmount() * 1f / 250;

                    // 1.20.1 Potion Logic
                    List<MobEffectInstance> effects = PotionUtils.getAllEffects(fluidStack.getTag());

                    for (MobEffectInstance effect : effects) {
                        if (effect.getEffect().isInstantenous()) {
                            effect.getEffect().applyInstantenousEffect(player, player, entityLiving, effect.getAmplifier(), lengthAmplifier);
                        } else {
                            MobEffectInstance newEffect = new MobEffectInstance(effect.getEffect(), (int) (effect.getDuration() * lengthAmplifier), effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon());
                            entityLiving.addEffect(newEffect);
                        }
                    }
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
        Optional<IFluidHandlerItem> from = FluidUtil.getFluidHandler(stack).resolve();
        Optional<IFluidHandlerItem> to = FluidUtil.getFluidHandler(slot.getItem()).resolve();
        if (from.isPresent() && to.isPresent()) {
            if (!FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, false).isEmpty()) {
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
        Optional<IFluidHandlerItem> from = FluidUtil.getFluidHandler(other).resolve();
        Optional<IFluidHandlerItem> to = FluidUtil.getFluidHandler(stack).resolve();
        if (from.isPresent() && to.isPresent()) {
            if (!FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, false).isEmpty()) {
                FluidUtil.tryFluidTransfer(to.get(), from.get(), 1000, true);
                access.set(from.get().getContainer());
                slot.set(to.get().getContainer());
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);

        Optional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack).resolve();
        if (handler.isEmpty()) return;

        FluidStack fluidStack = handler.get().getFluidInTank(0);
        final int color;

        if (fluidStack.getFluid() == Fluids.WATER)
            color = 0xFF3F76E4;
        else if (fluidStack.getFluid() == Fluids.LAVA) // Corrected from duplicate WATER check to LAVA if that was intent, or just cleaned up
            color = 0xFFFF6100;
        else
            color = fluidStack.getFluid().defaultFluidState().createLegacyBlock().getMapColor(VirtualLevel.getLevel(), BlockPos.ZERO).col;

        Component fluidName = fluidStack.getDisplayName();
        Component amount = Component.literal(String.valueOf(fluidStack.getAmount()));

        if (!fluidStack.isEmpty()) {
            tooltipComponents.add(CommonComponents.EMPTY);
            tooltipComponents.add(Component.translatable("container.sturdy_tank.tooltip.content_title").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("container.sturdy_tank.tooltip.content", amount, fluidName)
                    .withStyle(style -> style.withColor(color)));
        }

        Potion potion = PotionUtils.getPotion(fluidStack.getTag());
        if (potion != Potions.WATER) {
            tooltipComponents.add(Component.translatable("container.sturdy_tank.tooltip.potion_title").withStyle(ChatFormatting.GRAY));
            PotionUtils.addPotionTooltip(potion.getEffects(), tooltipComponents, 1f);
        }
    }
}