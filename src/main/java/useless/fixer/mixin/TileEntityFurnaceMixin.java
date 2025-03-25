package useless.fixer.mixin;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityFurnace.class, remap = false)
public abstract class TileEntityFurnaceMixin extends TileEntity {

	@Shadow
	protected ItemStack[] furnaceItemStacks;

	/**
	 * @author UselessBullets
	 * @reason Fixes empty furnaces excessively sending packets
	 */
	@Overwrite
	@Override
	public Packet getDescriptionPacket() {
		if (this.furnaceItemStacks[2] != null) {
			return new PacketTileEntityData(this);
		}
		return null;
	}
}
