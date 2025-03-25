package useless.fixer.mixin;

import net.minecraft.core.block.BlockLogicLamp;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicLamp.class, remap = false)
public class BlockLogicLampMixin {
	@Inject(method = "onBlockRightClicked", at = @At("HEAD"), cancellable = true)
	public void emptyHandOnlyInversion(final World world, final int x, final int y, final int z, final Player player, final Side side, final double xHit, final double yHit, final CallbackInfoReturnable<Boolean> cir) {
		if (player.getHeldItem() != null) cir.setReturnValue(false);
	}
}
