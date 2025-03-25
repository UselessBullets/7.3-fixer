package useless.fixer.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.projectile.ProjectileEgg;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityDispatcher.class, remap = false)
public abstract class EntityDispatcherMixin {
	@Shadow
	public static void addMapping(@NotNull final Class<? extends Entity> entityClass, @NotNull final NamespaceID namespaceID) {

	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityDispatcher;addMapping(Ljava/lang/Class;Lnet/minecraft/core/util/collection/NamespaceID;)V", ordinal = 0))
	private static void addEggMapping(CallbackInfo ci) {
		addMapping(ProjectileEgg.class, NamespaceID.getPermanent("minecraft", "egg"));
	}
}
