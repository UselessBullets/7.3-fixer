package useless.fixer.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.block.model.BlockModelChest;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.render.block.model.BlockModel.renderBlocks;

@Mixin(value = BlockModelChest.class, remap = false)
public class BlockModelChestMixin {
	@Shadow
	protected IconCoordinate chestTopRight;

	@Shadow
	protected IconCoordinate chestTopLeft;

	@Shadow
	protected IconCoordinate chestTop;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/model/BlockModelChest;renderStandardBlock(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/core/util/phys/AABB;III)Z", shift = At.Shift.BEFORE))
	public void addBottomRotations(final Tessellator tessellator, final int x, final int y, final int z, final CallbackInfoReturnable<Boolean> cir, @Local(name = "dir") final Direction direction) {
		switch (direction) {
			case NORTH:
				renderBlocks.uvRotateBottom = 3;
				break;

			case EAST:
				renderBlocks.uvRotateBottom = 1;
				break;

			case WEST:
				renderBlocks.uvRotateBottom = 2;
				break;
		}
	}

	@Inject(method = "getBlockTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogicChest;getTypeFromMeta(I)Lnet/minecraft/core/block/BlockLogicChest$Type;", shift = At.Shift.AFTER), cancellable = true)
	public void fixTextureGet(final WorldSource blockAccess, final int x, final int y, final int z, final Side side, final CallbackInfoReturnable<IconCoordinate> cir, @Local(name = "meta") final int meta) {
		if(side == Side.TOP || side == Side.BOTTOM) {
			BlockLogicChest.Type type = BlockLogicChest.getTypeFromMeta(meta);
			if(type == BlockLogicChest.Type.RIGHT) {
				cir.setReturnValue(this.chestTopRight);
				return;
			}
			if(type == BlockLogicChest.Type.LEFT) {
				cir.setReturnValue(this.chestTopLeft);
				return;
			}

			cir.setReturnValue(this.chestTop);
		}
	}
}
