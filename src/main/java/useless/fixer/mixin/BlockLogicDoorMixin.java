package useless.fixer.mixin;

import net.minecraft.core.block.BlockLogicDoor;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockLogicDoor.class, remap = false)
public class BlockLogicDoorMixin {
	@Redirect(method = "setColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockAndMetadata(IIIII)Z"))
	public boolean fixDoors(World instance, int x, int y, int z, int id, int meta) {
		return instance.setBlockAndMetadataRaw(x, y, z, id, meta);
	}
}
