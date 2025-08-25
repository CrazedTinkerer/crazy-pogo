package io.github.crazedtinkerer.crazy_pogo;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PogoStick extends Item {
	public PogoStick(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		// This would use useOnBlock instead, but that doesn't work in adventure mode
		// without canPlaceOn, and the pogo stick should still be usable there
		ItemStack usedItem = user.getStackInHand(hand);
		HitResult hitResult = user.raycast(user.getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE), 0, false);

		if (hitResult.getType() == HitResult.Type.BLOCK){
            bounceUser(user);
			applyCooldown(usedItem, user);
			return ActionResult.SUCCESS;
		} else {
			return super.use(world, user, hand);
		}
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		Vec3d bounceVelocity = bounceUser(user);

		bounceVelocity = bounceVelocity.negate().multiply(2);

		double minYVelocity = 0.1;
		if (entity.isOnGround() && bounceVelocity.y < minYVelocity){
			bounceVelocity = new Vec3d(bounceVelocity.x, minYVelocity, bounceVelocity.z);
		}

		entity.addVelocity(bounceVelocity);

		applyCooldown(stack, user);
		return ActionResult.SUCCESS;
	}

	/**
	 *
	 * @param user the entity using the pogo stick
	 * @return a Vec3d representing the velocity that was applied to the user
	 */
	private Vec3d bounceUser(LivingEntity user){
		World world = user.getWorld();
		Vec3d oldVelocity = user.getVelocity();
		Vec3d lookVector = Vec3d.fromPolar(user.getPitch(), user.getYaw());

		world.playSound(
				user,
				user.getX(),
				user.getY(),
				user.getZ(),
				SoundEvents.ENTITY_SLIME_SQUISH,
				SoundCategory.NEUTRAL,
				0.5F,
				0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
		);

		double bounceMultiplier = user.isSneaking() ? 1.5 : 2;
		double bounceSpeed = Math.max(oldVelocity.length(), 0.3) * bounceMultiplier;
		Vec3d bounceVelocity = lookVector.multiply(-1 * bounceSpeed);
		user.addVelocity(bounceVelocity);

		return bounceVelocity;
	}

	private void applyCooldown(ItemStack stack, LivingEntity user){
		UseCooldownComponent useCooldownComponent = stack.get(DataComponentTypes.USE_COOLDOWN);

		if (useCooldownComponent != null) {
			useCooldownComponent.set(stack, user);
		}
	}
}
