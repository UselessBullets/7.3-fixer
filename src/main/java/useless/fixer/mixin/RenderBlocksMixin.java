package useless.fixer.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.util.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderBlocks.class, remap = false)
public class RenderBlocksMixin {
	@Redirect(method = "renderBottomFace", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/texture/stitcher/IconCoordinate;getSubIconU(D)D", ordinal = 6))
	public double redirect1(IconCoordinate instance, double offset, @Local(name = "bounds") AABB bounds) {
		return instance.getSubIconU(1 - bounds.minX);
	}

	@Redirect(method = "renderBottomFace", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/texture/stitcher/IconCoordinate;getSubIconU(D)D", ordinal = 7))
	public double redirect2(IconCoordinate instance, double offset, @Local(name = "bounds") AABB bounds) {
		return instance.getSubIconU(1 - bounds.maxX);
	}

}
