package useless.fixer.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.block.BlockLogicDoor;
import net.minecraft.core.util.helper.Side;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerController.class, remap = false)
public abstract class PlayerControllerMixin { // Fixes the double click issue

	@Shadow
	protected abstract void setMineBlock(Integer x, Integer y, Integer z);

	@Shadow
	@Final
	protected Minecraft mc;

	@Shadow
	protected abstract void hitBlock(int x, int y, int z, Side side, double xHit, double yHit);

	@Inject(method = "startDestroyBlock(IIILnet/minecraft/core/util/helper/Side;DDZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/controller/PlayerController;hitBlock(IIILnet/minecraft/core/util/helper/Side;DD)V", shift = At.Shift.AFTER))
	public void setMineBlockOnStart(int x, int y, int z, Side side, double xHit, double yHit, boolean repeat, CallbackInfo ci) {
		setMineBlock(x, y, z);
	}

	@Redirect(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/controller/PlayerController;hitBlock(IIILnet/minecraft/core/util/helper/Side;DD)V"))
	public void redirectHit(PlayerController instance, int x, int y, int z, Side side, double xHit, double yHit) {
		if (mc.currentWorld.getBlockLogic(x, y, z, BlockLogicDoor.class) == null) { // "Fixes" issue where at certain angles you can spam hit the top and bottom blocks of the door.
			hitBlock(x, y, z, side, xHit, yHit);
		}
	}

	@Inject(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/controller/PlayerController;sendDestroyBlockPacket(IIILnet/minecraft/core/util/helper/Side;DD)V", shift = At.Shift.AFTER))
	public void nullOutOldBlockLocation(int x, int y, int z, Side side, double xHit, double yHit, CallbackInfo ci) {
		setMineBlock(null, null, null);
	}
}
