2
https://raw.githubusercontent.com/KarogathModdingStudios/Enhanced-Vanilla-1.15/master/src/main/java/com/karogath/enhancedvanilla/block/CharredNetherBrickStairsBlock.java

package com.karogath.enhancedvanilla.block;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import java.util.List;
import java.util.Collections;

import com.karogath.enhancedvanilla.EnhancedVanillaElements;

@EnhancedVanillaElements.ModElement.Tag
public class CharredNetherBrickStairsBlock extends EnhancedVanillaElements.ModElement {
	@ObjectHolder("enhancedvanilla:charred_nether_brick_stairs")
	public static final Block block = null;
	public CharredNetherBrickStairsBlock(EnhancedVanillaElements instance) {
		super(instance, 3);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items
				.add(() -> new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(block.getRegistryName()));
	}
	public static class CustomBlock extends StairsBlock {
		public CustomBlock() {
			super(new Block(Block.Properties.create(Material.ROCK)).getDefaultState(), Block.Properties.create(Material.ROCK).sound(SoundType.STONE)
					.hardnessAndResistance(1f, 10f).lightValue(0).harvestLevel(1).harvestTool(ToolType.PICKAXE));
			setRegistryName("charred_nether_brick_stairs");
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 0));
		}
	}
}