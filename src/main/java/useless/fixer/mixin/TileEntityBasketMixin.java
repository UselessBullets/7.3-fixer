package useless.fixer.mixin;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityBasket.class, remap = false)
public abstract class TileEntityBasketMixin extends TileEntity {
	@Shadow
	private int numUnitsInside;

	/**
	 * @author UselessBullets
	 * @reason Fixes unnecessary packet sending for empty baskets
	 */
	@Overwrite
	public Packet getDescriptionPacket() {
		if (this.numUnitsInside > 0) {
			return new PacketTileEntityData(this);
		} else {
			return null;
		}
	}
}
