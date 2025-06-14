package net.mcreator.concoction.potion;

import com.mojang.datafixers.util.Pair;
import net.mcreator.concoction.ConcoctionMod;
import net.mcreator.concoction.init.ConcoctionModMobEffects;
import net.mcreator.concoction.utils.Utils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.List;

public class SpicyMobEffect extends MobEffect {

	private static int tickCounter = 0;
	private static final ResourceLocation SPICY_ATTACK_SPEED = ResourceLocation.fromNamespaceAndPath("concoction", "spicy_attack_speed");

	public SpicyMobEffect() {
		super(MobEffectCategory.NEUTRAL, -46336);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
		AttributeInstance attackSpeedAttribute = attributeMap.getInstance(Attributes.ATTACK_SPEED);
		
		if (attackSpeedAttribute != null) {
			double increaseValue = (amplifier + 1) * 0.15;
			
			AttributeModifier attackSpeedModifier = new AttributeModifier(
					SPICY_ATTACK_SPEED,
					increaseValue,
					AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
			);
			
			attackSpeedAttribute.addTransientModifier(attackSpeedModifier);
		}
		
		super.addAttributeModifiers(attributeMap, amplifier);
	}
	
	@Override
	public void removeAttributeModifiers(AttributeMap attributeMap) {
		AttributeInstance attackSpeedAttribute = attributeMap.getInstance(Attributes.ATTACK_SPEED);
		
		if (attackSpeedAttribute != null) {
			attackSpeedAttribute.removeModifier(SPICY_ATTACK_SPEED);
		}
		
		super.removeAttributeModifiers(attributeMap);
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {

		LevelAccessor world = entity.level();

		if (entity instanceof LivingEntity livEnt) {
			if (livEnt.hasEffect(ConcoctionModMobEffects.SPICY)) {
				if (world instanceof Level pLevel && !pLevel.isClientSide() && livEnt instanceof ServerPlayer player) {

					int tickInterval = 60 - amplifier * 10;
					if (tickInterval < 20) tickInterval = 20;

					if (tickCounter >= tickInterval && player.getHealth() > 1.0) {
						livEnt.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("concoction:spicy_damage")))),
								1.0f);
						tickCounter = 0;
					}

					List<Pair<Holder<MobEffect>, MobEffectCategory>> list = livEnt.getActiveEffects().stream().map(effect -> new Pair<>(effect.getEffect(), effect.getEffect().value().getCategory()))
							.filter(pair -> pair.getSecond().equals(MobEffectCategory.HARMFUL))
							.distinct()
							.toList();

					if (list.size() >= 5) {
						Utils.addAchievement(player, "concoction:spicy_remove_many_debuffs");
					}
					list.forEach(pair -> {
						livEnt.removeEffect(pair.getFirst());
					});


					if (player.isOnFire()) {
						Utils.addAchievement(player, "concoction:spicy_on_fire");
					}
					++tickCounter;
				}
			}
		}
		return super.applyEffectTick(entity, amplifier);
	}
}
