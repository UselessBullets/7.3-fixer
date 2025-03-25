package useless.fixer.mixin;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ScreenAddServer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScreenAddServer.class, remap = false)
public abstract class ScreenAddServerMixin {
	@Shadow
	private ButtonElement buttonSave;

	@Shadow
	protected abstract void buttonClicked(ButtonElement button);

	@Inject(method = "keyPressed", at = @At(value = "HEAD"))
	public void acceptKPEnter(final char eventCharacter, final int eventKey, final int mx, final int my, final CallbackInfo ci) {
		if (eventKey == Keyboard.KEY_NUMPADENTER && this.buttonSave.enabled) {
			buttonClicked(this.buttonSave);
		}
	}
}
