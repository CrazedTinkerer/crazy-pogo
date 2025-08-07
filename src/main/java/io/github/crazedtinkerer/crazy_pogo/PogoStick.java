package io.github.crazedtinkerer.crazy_pogo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PogoStick extends Item {
	public PogoStick(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context){
		PlayerEntity player = context.getPlayer();
		if (player == null){
			return ActionResult.PASS;
		}

		Vec3d oldVelocity = player.getVelocity();
		Vec3d lookVector = Vec3d.fromPolar(player.getPitch(), player.getYaw());

		double bounceSpeed = oldVelocity.length() * 2; //TODO: Reduce bounce force if player is sneaking?
		player.addVelocity(lookVector.multiply(-1 * bounceSpeed));
		World world = context.getWorld();
		world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.NEUTRAL, 0.5F, 0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

		return ActionResult.SUCCESS;
	}
}
