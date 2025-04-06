package useless.fixer.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.packet.PacketMovePlayer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.server.net.handler.PacketHandlerServer.LOGGER;

@Mixin(value = PacketHandlerServer.class, remap = false)
public abstract class PacketHandlerServerMixin {

	@Shadow
	private double lastPosX;

	@Shadow
	private double lastPosY;

	@Shadow
	private double lastPosZ;

	@Shadow
	private PlayerServer playerEntity;

	@Shadow
	@Final
	private MinecraftServer mcServer;

	@Shadow
	private boolean hasMoved;

	@Shadow
	public abstract void teleportAndRotate(double x, double y, double z, float yaw, float pitch);

	@Shadow
	private int playerInAirTime;

	@Shadow
	public abstract void kickPlayer(String s);

	public void handleFlying(PacketMovePlayer packet) {
		if (this.playerEntity.isAlive()) {
			WorldServer worldserver = this.mcServer.getDimensionWorld(this.playerEntity.dimension);
			if (!this.hasMoved) {
				double dx = packet.x - this.lastPosX;
				double dy = packet.y - this.lastPosY;
				double dz = packet.z - this.lastPosZ;
				dx *= dx;
				dy *= dy;
				dz *= dz;
				if (dx * dx < 0.01 && dy * dy < 0.01 && dz * dz < 0.01) {
					this.hasMoved = true;
				}
			}

			if (this.hasMoved) {
				float yRot = this.playerEntity.yRot;
				float xRot = this.playerEntity.xRot;
				if (packet.hasRotation) {
					yRot = MathHelper.normalizeRotation(packet.yaw);
					xRot = MathHelper.normalizeRotation(packet.pitch);
				}

				boolean sleeping = this.playerEntity.isPlayerSleeping();
				boolean sitting = this.playerEntity.vehicle != null && !(this.playerEntity.vehicle instanceof Entity);
				if (!sleeping && !sitting) {
					if (this.playerEntity.vehicle instanceof Entity) {
						this.playerEntity.vehicle.positionRider();
						double xd = (double)0.0F;
						double zd = (double)0.0F;
						if (packet.hasPosition && packet.y == (double)-999.0F) {
							xd = packet.x;
							zd = packet.z;
						}

						this.playerEntity.onGround = packet.onGround;
						this.playerEntity.onUpdateEntity();
						this.playerEntity.move(xd, (double)0.0F, zd);
						this.playerEntity.absMoveTo(this.playerEntity.x, this.playerEntity.y, this.playerEntity.z, yRot, xRot);
						this.playerEntity.xd = xd;
						this.playerEntity.zd = zd;
						worldserver.updateEntityWithOptionalForce((Entity)this.playerEntity.vehicle, false);
						this.playerEntity.vehicle.positionRider();
						this.mcServer.playerList.onPlayerMoved(this.playerEntity);
					} else {
						this.lastPosX = this.playerEntity.x;
						this.lastPosY = this.playerEntity.y;
						this.lastPosZ = this.playerEntity.z;
						double newPosX = this.playerEntity.x;
						double newPosY = this.playerEntity.y;
						double oldPosY = this.playerEntity.y;
						double newPosZ = this.playerEntity.z;
						if (packet.hasPosition && packet.y == (double)-999.0F) {
							packet.hasPosition = false;
						}

						if (packet.hasPosition) {
							newPosX = packet.x;
							newPosY = packet.y;
							newPosZ = packet.z;
							if (Math.abs(packet.x) > (double)3.2E7F || Math.abs(packet.z) > (double)3.2E7F || Double.isNaN(packet.x) || Double.isNaN(packet.y) || Double.isNaN(packet.z)) {
								LOGGER.warn("{} tried to move to an illegal position", this.playerEntity.username);
								this.teleportAndRotate(this.lastPosX, this.lastPosY, this.lastPosZ, yRot, xRot);
								return;
							}
						}

						this.playerEntity.onUpdateEntity();
						this.playerEntity.ySlideOffset = 0.0F;
						this.playerEntity.absMoveTo(this.lastPosX, this.lastPosY, this.lastPosZ, yRot, xRot);
						if (this.hasMoved) {
							double dx = newPosX - this.playerEntity.x;
							double dy = newPosY - this.playerEntity.y;
							double dz = newPosZ - this.playerEntity.z;
							double velSquared = dx * dx + dy * dy + dz * dz;
							if (velSquared > (double)100.0F) {
								LOGGER.warn("{} moved too quickly!", this.playerEntity.username);
								this.teleportAndRotate(this.lastPosX, this.lastPosY, this.lastPosZ, yRot, xRot);
							} else {
								float bb_expand = 0.0625F;
								boolean insideBlockOld = worldserver.getCubes(this.playerEntity, this.playerEntity.bb.copy().getInsetBoundingBox((double)bb_expand, (double)bb_expand, (double)bb_expand)).isEmpty();
								this.playerEntity.move(dx, dy, dz);

								dx = newPosX - this.playerEntity.x;
								double ndy = newPosY - this.playerEntity.y;
								dz = newPosZ - this.playerEntity.z;

								// We don't care about verify the max velocity of falling
								if (ndy < -0.0784) {
									ndy = -0.0784;
								}

								velSquared = dx * dx + ndy * ndy + dz * dz;
								boolean movedWrong = false;
								if (this.playerEntity.pushTime < 0.1F && !this.playerEntity.getGamemode().canPlayerFly() && velSquared > (double)0.0625F && !this.playerEntity.isPlayerSleeping()) {
									movedWrong = true;
									LOGGER.warn("{} moved wrongly!", this.playerEntity.username);
									LOGGER.warn("Got position {}, {}, {}", new Object[]{newPosX, newPosY, newPosZ});
									LOGGER.warn("Expected {}, {}, {}", new Object[]{this.playerEntity.x, this.playerEntity.y, this.playerEntity.z});
								}

								this.playerEntity.absMoveTo(newPosX, newPosY, newPosZ, yRot, xRot);
								boolean insideBlockNew = worldserver.getCubes(this.playerEntity, this.playerEntity.bb.copy().getInsetBoundingBox((double)bb_expand, (double)bb_expand, (double)bb_expand)).isEmpty();
								if (this.playerEntity.pushTime < 0.1F && !this.playerEntity.getGamemode().canPlayerFly() && insideBlockOld && (movedWrong || !insideBlockNew) && !this.playerEntity.isPlayerSleeping()) {
									this.teleportAndRotate(this.lastPosX, this.lastPosY, this.lastPosZ, yRot, xRot);
								} else {
									AABB aabb = this.playerEntity.bb.copy().grow((double)bb_expand, (double)bb_expand, (double)bb_expand).expand((double)0.0F, -0.55, (double)0.0F);
									if (!this.playerEntity.getGamemode().canPlayerFly() && !this.mcServer.allowFlight && !worldserver.getIsAnySolidGround(aabb)) {
										if (dy > (double)-0.03125F) {
											++this.playerInAirTime;
											if (this.playerInAirTime > 100) {
												LOGGER.warn(this.playerEntity.username + " was kicked for floating too long!");
												this.kickPlayer("Flying is not enabled on this server");
												return;
											}
										}
									} else {
										this.playerInAirTime = 0;
									}

									this.playerEntity.onGround = packet.onGround;
									this.mcServer.playerList.onPlayerMoved(this.playerEntity);
									this.playerEntity.handleFalling(this.playerEntity.y - oldPosY, packet.onGround);
								}
							}
						}
					}
				} else {
					if (sitting) {
						this.playerEntity.vehicle.positionRider();
					}

					this.playerEntity.onUpdateEntity();
					this.playerEntity.absMoveTo(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.yRot, this.playerEntity.xRot);
					worldserver.updateEntity(this.playerEntity);
				}
			}
		}
	}
}
