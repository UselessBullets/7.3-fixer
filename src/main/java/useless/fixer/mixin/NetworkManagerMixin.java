package useless.fixer.mixin;

import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.net.handler.PacketHandler;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketBlockRegionUpdate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.fixer.interfaces.IBuffer;

import java.util.List;

@Mixin(value = NetworkManager.class, remap = false)
public abstract class NetworkManagerMixin {
	@Shadow
	@Final
	private List<Packet> incoming;

	@Shadow
	private boolean disconnected;

	@Shadow
	private PacketHandler packetListener;

	@Inject(method = "processReadPackets()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/net/NetworkManager;wakeThreads()V", shift = At.Shift.BEFORE))
	public void increaseIncomingThroughput(CallbackInfo ci) {
		while (!this.incoming.isEmpty()) {
			final Packet packet = this.incoming.remove(0);
			if (!this.disconnected) {
				packet.handlePacket(this.packetListener);
			}
		}
	}

	@Inject(method = "networkShutdown", at = @At("HEAD"))
	public void clearPacketCache(String reason, Object[] reasonObjects, CallbackInfo ci) {
		((IBuffer)new PacketBlockRegionUpdate()).btafix$destroyCache();
	}
}
