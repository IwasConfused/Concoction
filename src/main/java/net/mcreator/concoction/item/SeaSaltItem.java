
package net.mcreator.concoction.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class SeaSaltItem extends Item {
	public SeaSaltItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
