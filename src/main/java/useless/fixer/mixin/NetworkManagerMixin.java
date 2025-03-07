package useless.fixer.mixin;

import net.minecraft.core.net.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = NetworkManager.class, remap = false)
public abstract class NetworkManagerMixin {
	/*
	@Mutable
	@Shadow
	@Final
	private List<Packet> incoming;

	@Shadow
	@Final
	private List<Packet> outgoing;

	@Inject(method = "processReadPackets", at = @At("HEAD"))
	public void size(CallbackInfo ci){
		Map<String, Integer> map = new HashMap<>();

		int max = -1;
		String maxName = "";
		for (int i = 0; i < this.incoming.size(); i++) {
			Packet packet = this.incoming.get(i);

			String name = this.incoming.get(i).getClass().getSimpleName();
			int val = map.getOrDefault(name, 0) + 1;
			map.put(name, val);

			if(val > max){
				max = val;
				maxName = name;
			}
		}
		StringBuilder out = new StringBuilder();
		for (Map.Entry<String, Integer> e : map.entrySet()){
			out.append(e.getKey()).append(": ").append(e.getValue()).append(" | ");
		}
		System.out.println("\t" + this.incoming.size() + "\t" + this.outgoing.size() + "\t | " + out);
	}
	 */

	@ModifyConstant(method = "processReadPackets", constant = @Constant(intValue = 100))
	private int mod(int constant){
		return 1000;
	}
}
