package useless.fixer.mixin;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.util.dispatch.Dispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockModelDispatcher.class, remap = false)
public abstract class BlockModelDispatcherMixin extends Dispatcher<Block<?>, BlockModel<?>> {
	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void fixRetroDiamondOre(final CallbackInfo ci) {
		final BlockModel model = getDispatch(Blocks.ORE_DIAMOND_STONE);
		if (model instanceof BlockModelStandard) {
			final BlockModelStandard ms = (BlockModelStandard) model;
			ms.setAllTextures(BlockModelStandard.RETRO_BLOCK_TEXTURES, "minecraft:block/ore/diamond/stone_retro");
		}
	}
}
