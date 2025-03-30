package useless.fixer.mixin;

import net.minecraft.core.Global;
import net.minecraft.core.block.piston.BlockLogicPistonBaseSteel;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockLogicPistonBaseSteel.class, remap = false)
public class BlockLogicPistonBaseSteelMixin {
	@Redirect(
		method = "extendEvent",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/entity/Entity;fling(DDDF)V"
		)
	)
	private void redirectFling(Entity entity, double x, double y, double z, float strength) {
		if (!Global.isServer && entity.world != null && entity instanceof Player && entity.world.isClientSide) {
			return;
		}

		entity.fling(x, y, z, strength);
	}
}
