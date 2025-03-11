package useless.fixer.mixin;

import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.net.packet.PacketBlockRegionUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.fixer.interfaces.IBuffer;

@Mixin(value = NetworkManager.class, remap = false)
public abstract class NetworkManagerMixin {
	@ModifyConstant(method = "processReadPackets", constant = @Constant(intValue = 100))
	private int mod(int constant){
		return 1000;
	}

	@Inject(method = "networkShutdown", at = @At("HEAD"))
	public void clearPacketCache(String reason, Object[] reasonObjects, CallbackInfo ci) {
		((IBuffer)new PacketBlockRegionUpdate()).btafix$destroyCache();
	}
}
