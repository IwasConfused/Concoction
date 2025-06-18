
package net.mcreator.concoction.block;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

import com.mojang.serialization.MapCodec;

public class SeaSaltBlockBlock extends FallingBlock {
	public static final MapCodec<SeaSaltBlockBlock> CODEC = simpleCodec(properties -> new SeaSaltBlockBlock());

	public MapCodec<SeaSaltBlockBlock> codec() {
		return CODEC;
	}

	public SeaSaltBlockBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).sound(SoundType.SAND).strength(0.4f, 0.5f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
