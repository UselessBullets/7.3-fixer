package useless.fixer.mixin;

import net.minecraft.client.gui.popup.PopupScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PopupScreen.class, remap = false)
public abstract class PopupScreenMixin {
	@Shadow
	@Final
	private Integer statusCodeOnEnter;

	@Shadow
	abstract void close(int statusCode);

	@Inject(method = "keyPressed", at = @At("HEAD"))
	public void addKPEnter(char eventCharacter, int eventKey, int mx, int my, CallbackInfo ci) {
		if (eventKey == Keyboard.KEY_NUMPADENTER && this.statusCodeOnEnter != null) {
			this.close(this.statusCodeOnEnter);
		}
	}
}
