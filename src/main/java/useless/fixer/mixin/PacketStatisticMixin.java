package useless.fixer.mixin;

import net.minecraft.core.net.packet.PacketStatistic;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PacketStatistic.class, remap = false)
public class PacketStatisticMixin { // Crash packet fix
	@Redirect(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/collection/NamespaceID;getTemp(Ljava/lang/String;)Lnet/minecraft/core/util/collection/NamespaceID;"))
	public NamespaceID fixInvalidNamespaceGet(String formattedKey) throws HardIllegalArgumentException {
		return NamespaceID.getPermanent(formattedKey);
	}
}
