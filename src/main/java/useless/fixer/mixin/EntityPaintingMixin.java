package useless.fixer.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityPainting;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPainting.class, remap = false)
public abstract class EntityPaintingMixin extends Entity { // Painting Drop fix
	public EntityPaintingMixin(@Nullable World world) {
		super(world);
	}

	@Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityPainting;remove()V", ordinal = 0))
	private void stopFirstRemoveCall(EntityPainting instance) {

	}

	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playBlockSoundEffect(Lnet/minecraft/core/entity/Entity;DDDLnet/minecraft/core/block/Block;Lnet/minecraft/core/enums/EnumBlockSoundEffectType;)V", shift = At.Shift.BEFORE))
	private void addNewRemoveCall(Entity entity, int i, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		this.remove();
	}
}
