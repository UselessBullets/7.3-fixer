package useless.fixer.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.core.net.packet.PacketMovePlayer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandlerServer.class, remap = false)
public abstract class PacketHandlerServerMixin {
	@Inject(method = "handleFlying", at = @At(value = "FIELD", target = "Lnet/minecraft/server/entity/player/PlayerServer;z:D", ordinal = 4, shift = At.Shift.AFTER))
	private void limitSpeedCheckYNegative(PacketMovePlayer packet, CallbackInfo ci, @Local(name = "ndy") LocalDoubleRef ndy) {
		if (ndy.get() < -0.0784) {
			ndy.set(-0.0784);
		}
	}
}
