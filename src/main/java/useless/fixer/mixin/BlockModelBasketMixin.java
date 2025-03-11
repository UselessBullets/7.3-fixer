package useless.fixer.mixin;

import net.minecraft.client.render.block.model.BlockModelBasket;
import net.minecraft.core.block.BlockLogicBasket;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockModelBasket.class, remap = false)
public class BlockModelBasketMixin {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogicBasket;getFillLevel(Lnet/minecraft/core/world/WorldSource;III)I"))
	public int getFillLevel(BlockLogicBasket instance, WorldSource world, int x, int y, int z) {
		if(world.getTileEntity(x, y, z) instanceof TileEntityBasket){
			TileEntityBasket te = (TileEntityBasket)world.getTileEntity(x, y, z);

			float fill = MathHelper.clamp((float)te.getNumUnitsInside() / (float)te.getMaxUnits(), 0.0F, 1.0F);

			return (int)Math.ceil((double)(10.0F * fill));
		} else {
			return 0;
		}
	}
}
