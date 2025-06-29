package net.mcreator.concoction.procedures;

import net.mcreator.concoction.block.SoullandBlock;
import net.mcreator.concoction.item.OvergrownHoeItem;
import net.mcreator.concoction.utils.Utils;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.mcreator.concoction.init.ConcoctionModBlocks;
import org.apache.logging.log4j.core.jmx.Server;

import javax.annotation.Nullable;
import java.util.Objects;

@EventBusSubscriber
public class SoullandHoeRightclickProcedure {
	@SubscribeEvent
	public static void onUseHoe(BlockEvent.BlockToolModificationEvent event) {

		ItemStack itemStack;
		ItemStack itemStack2;

		try {
			itemStack = Objects.requireNonNull(event.getPlayer()).getItemInHand(InteractionHand.MAIN_HAND);
			itemStack2 = Objects.requireNonNull(event.getPlayer()).getItemInHand(InteractionHand.OFF_HAND);
		} catch (NullPointerException e) {
			return;
		}
		if ( itemStack.getItem() instanceof OvergrownHoeItem item) {
			if (item.getDamage(itemStack) >= item.getMaxDamage(itemStack) - 1) {
				event.setCanceled(true);
			}
		}

		if ( itemStack2.getItem() instanceof OvergrownHoeItem item) {
			if (item.getDamage(itemStack2) >= item.getMaxDamage(itemStack2) - 1) {
				event.setCanceled(true);
			}
		}

		if (!event.isSimulated() && event.getItemAbility() == ItemAbilities.HOE_TILL && event.getPlayer() != null) {
			execute(event, event.getContext().getLevel(), event.getContext().getClickedPos().getX(), event.getContext().getClickedPos().getY(), event.getContext().getClickedPos().getZ(), event.getPlayer());

			if (!event.getPlayer().level().isClientSide() && event.getPlayer() instanceof ServerPlayer serverPlayer && event.getState().getBlock() == Blocks.SOUL_SOIL) {
				Utils.addAchievement(serverPlayer, "concoction:make_soul_soul");
			}


		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.SOUL_SOIL && !world.isClientSide()) {
			
			if (entity instanceof ServerPlayer serverPlayer) {
				ItemStack itemStack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
				ItemStack itemStack2 = serverPlayer.getItemInHand(InteractionHand.OFF_HAND);

				if (itemStack.getItem() instanceof OvergrownHoeItem item) {
					if (item.getDamage(itemStack) >= item.getMaxDamage(itemStack) - 1) {
						return;
					}
				}
				
				if (itemStack2.getItem() instanceof OvergrownHoeItem item) {
					if (item.getDamage(itemStack2) >= item.getMaxDamage(itemStack2) - 1) {
						return;
					}
				}
			}

			world.setBlock(BlockPos.containing(x, y, z), ConcoctionModBlocks.SOULLAND.get().defaultBlockState(), 3);
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof HoeItem) {

				if (entity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
				if (!(new Object() {
					public boolean checkGamemode(Entity _ent) {
						if (_ent instanceof ServerPlayer _serverPlayer) {
							return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
						} else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
							return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
									&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
						}
						return false;
					}
				}.checkGamemode(entity))) {
					if (world instanceof ServerLevel _level) {

						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
				}
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() instanceof HoeItem) {
				if (entity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.OFF_HAND, true);
				if (!(new Object() {
					public boolean checkGamemode(Entity _ent) {
						if (_ent instanceof ServerPlayer _serverPlayer) {
							return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
						} else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
							return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
									&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
						}
						return false;
					}
				}.checkGamemode(entity))) {
					if (world instanceof ServerLevel _level) {


						(entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
				}
			} else {
				if (entity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
				if (!(new Object() {
					public boolean checkGamemode(Entity _ent) {
						if (_ent instanceof ServerPlayer _serverPlayer) {
							return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
						} else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
							return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
									&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
						}
						return false;
					}
				}.checkGamemode(entity))) {
					if (world instanceof ServerLevel _level) {


						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
				}
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.hoe.till")), SoundSource.PLAYERS, 1, (float) Math.random());
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.hoe.till")), SoundSource.PLAYERS, 1, (float) Math.random(), false);
				}
			}
		}
	}
}
