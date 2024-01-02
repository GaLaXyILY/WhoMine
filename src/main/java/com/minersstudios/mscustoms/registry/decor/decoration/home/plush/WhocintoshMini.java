package com.minersstudios.mscustoms.registry.decor.decoration.home.plush;

import com.minersstudios.mscore.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.mscore.inventory.recipe.builder.ShapedRecipeBuilder;
import com.minersstudios.mscore.utility.ChatUtils;
import com.minersstudios.mscustoms.sound.SoundGroup;
import com.minersstudios.mscustoms.custom.decor.CustomDecorDataImpl;
import com.minersstudios.mscustoms.custom.decor.DecorHitBox;
import com.minersstudios.mscustoms.custom.decor.Facing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class WhocintoshMini extends CustomDecorDataImpl<WhocintoshMini> {

    @Override
    protected @NotNull Builder builder() {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1370);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Мини-Whocintosh"));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key("whocintosh_mini")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.NONE)
                        .facings(
                                Facing.FLOOR,
                                Facing.WALL
                        )
                        .wallDirected(true)
                        .size(0.5d, 0.5625d, 0.5d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.WOOL)
                .itemStack(itemStack)
                .recipes(
                        unused -> Map.entry(
                                RecipeBuilder.shapedBuilder()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "WWW",
                                        "WGW"
                                )
                                .ingredients(
                                        ShapedRecipeBuilder.material('W', Material.GRAY_WOOL),
                                        ShapedRecipeBuilder.material('G', Material.GLASS_PANE)
                                ),
                                Boolean.TRUE
                        )
                );
    }
}
