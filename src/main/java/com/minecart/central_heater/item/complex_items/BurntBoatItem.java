package com.minecart.central_heater.item.complex_items;

import com.minecart.central_heater.entity.BurntBoat;
import com.minecart.central_heater.entity.BurntChestBoat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class BurntBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private final boolean hasChest;

    public BurntBoatItem(boolean hasChest, Properties properties) {
        super(properties);
        this.hasChest = hasChest;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        }

        Vec3 viewVec = player.getViewVector(1.0F);
        List<Entity> entities = level.getEntities(player, player.getBoundingBox().expandTowards(viewVec.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
        if (!entities.isEmpty()) {
            Vec3 eyePos = player.getEyePosition();
            for (Entity entity : entities) {
                AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (box.contains(eyePos)) {
                    return InteractionResultHolder.pass(itemstack);
                }
            }
        }

        if (hitresult.getType() == HitResult.Type.BLOCK) {
            Boat boat = createBoat(level, hitresult);
            boat.setYRot(player.getYRot());
            if (!level.noCollision(boat, boat.getBoundingBox())) {
                return InteractionResultHolder.fail(itemstack);
            }
            if (!level.isClientSide) {
                level.addFreshEntity(boat);
                level.gameEvent(player, net.minecraft.world.level.gameevent.GameEvent.ENTITY_PLACE, hitresult.getLocation());
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemstack);
    }

    private Boat createBoat(Level level, HitResult hitresult) {
        Vec3 loc = hitresult.getLocation();
        Boat boat = hasChest
                ? new BurntChestBoat(level, loc.x, loc.y, loc.z)
                : new BurntBoat(level, loc.x, loc.y, loc.z);
        return boat;
    }
}
