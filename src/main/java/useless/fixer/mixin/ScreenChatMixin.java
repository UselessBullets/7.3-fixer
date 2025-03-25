package useless.fixer.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.gui.chat.ScreenChat;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScreenChat.class, remap = false)
public abstract class ScreenChatMixin {
	@Shadow
	public abstract void keyPressed(char eventCharacter, int eventKey, int mx, int my);

	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
	public void acceptKPEnter(final char eventCharacter, final int eventKey, final int mx, final int my, final CallbackInfo ci) {
		if (eventKey == Keyboard.KEY_NUMPADENTER) {
			keyPressed(eventCharacter, Keyboard.KEY_RETURN, mx, my);
			ci.cancel();
		}
	}
}
