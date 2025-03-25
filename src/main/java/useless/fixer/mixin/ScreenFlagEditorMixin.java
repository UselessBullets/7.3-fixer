package useless.fixer.mixin;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.container.ScreenFlagEditor;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScreenFlagEditor.class, remap = false)
public abstract class ScreenFlagEditorMixin extends ScreenContainerAbstract {
	public ScreenFlagEditorMixin(MenuAbstract container) {
		super(container);
	}

	@Inject(method = "removed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/container/ScreenContainerAbstract;removed()V", shift = At.Shift.AFTER), cancellable = true)
	public void fixNPE(CallbackInfo ci) {
		if (this.mc.currentWorld == null) ci.cancel();
	}
}
