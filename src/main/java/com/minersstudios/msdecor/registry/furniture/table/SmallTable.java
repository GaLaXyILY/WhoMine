package com.minersstudios.msdecor.registry.furniture.table;

import com.minersstudios.mscore.inventory.recipe.RecipeBuilder;
import com.minersstudios.mscore.inventory.recipe.ShapedRecipeBuilder;
import com.minersstudios.mscore.util.ChatUtils;
import com.minersstudios.mscore.util.SoundGroup;
import com.minersstudios.msdecor.api.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class SmallTable<C extends CustomDecorData<C>> extends CustomDecorDataImpl<C> {

    protected final @NotNull Builder createBuilder(
            final @NotNull String key,
            final int customModelData,
            final @NotNull String displayName,
            final @NotNull Material planksMaterial
    ) {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(customModelData);
        itemMeta.displayName(ChatUtils.createDefaultStyledText(displayName));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key(key)
                .hitBox(new DecorHitBox(
                        1.0d,
                        1.0d,
                        1.0d,
                        DecorHitBox.Type.BARRIER
                ))
                .facing(Facing.FLOOR)
                .soundGroup(SoundGroup.WOOD)
                .itemStack(itemStack)
                .recipes(
                        builder -> Map.entry(
                                RecipeBuilder.shapedBuilder()
                                .namespacedKey(builder.key())
                                .group(CustomDecorType.NAMESPACE + ":small_table")
                                .category(CraftingBookCategory.BUILDING)
                                .result(builder.itemStack())
                                .shape(
                                        "PPP",
                                        "PAP"
                                )
                                .ingredients(
                                        ShapedRecipeBuilder.material('P', planksMaterial),
                                        ShapedRecipeBuilder.material('A', Material.AIR)
                                )
                                .build(),
                                true
                        )
                );
    }

    public static final class Acacia extends SmallTable<Acacia> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "acacia_small_table",
                    1070,
                    "Маленький акациевый стол",
                    Material.ACACIA_PLANKS
            );
        }
    }

    public static final class Birch extends SmallTable<Birch> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "birch_small_table",
                    1072,
                    "Маленький берёзовый стол",
                    Material.BIRCH_PLANKS
            );
        }
    }

    public static final class Cherry extends SmallTable<Cherry> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "cherry_small_table",
                    1384,
                    "Маленький вишнёвый стол",
                    Material.CHERRY_PLANKS
            );
        }
    }

    public static final class Crimson extends SmallTable<Crimson> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "crimson_small_table",
                    1074,
                    "Маленький багровый стол",
                    Material.CRIMSON_PLANKS
            );
        }
    }

    public static final class DarkOak extends SmallTable<DarkOak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "dark_oak_small_table",
                    1076,
                    "Маленький стол из тёмного дуба",
                    Material.DARK_OAK_PLANKS
            );
        }
    }

    public static final class Jungle extends SmallTable<Jungle> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "jungle_small_table",
                    1078,
                    "Маленький тропический стол",
                    Material.JUNGLE_PLANKS
            );
        }
    }

    public static final class Mangrove extends SmallTable<Mangrove> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "mangrove_small_table",
                    1201,
                    "Маленький мангровый стол",
                    Material.MANGROVE_PLANKS
            );
        }
    }

    public static final class Oak extends SmallTable<Oak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "oak_small_table",
                    1080,
                    "Маленький дубовый стол",
                    Material.OAK_PLANKS
            );
        }
    }

    public static final class Spruce extends SmallTable<Spruce> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "spruce_small_table",
                    1082,
                    "Маленький еловый стол",
                    Material.SPRUCE_PLANKS
            );
        }
    }

    public static final class Warped extends SmallTable<Warped> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "warped_small_table",
                    1084,
                    "Маленький искажённый стол",
                    Material.WARPED_PLANKS
            );
        }
    }
}
