package com.shimgo2008.botaniathuergia.blocks;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import com.shimgo2008.botaniathuergia.BotaniaTheurgiaMod;
import com.shimgo2008.botaniathuergia.recipe.PoorAltarRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class PoorAltarCoreBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    public PoorAltarCoreBlockEntity(BlockPos pos, BlockState state) {
        super(BotaniaTheurgiaMod.POOR_ALTAR_BE.get(), pos, state);
        LOGGER.info("Poor Altar BE created at {}", pos);
    }

    public static final Supplier<IMultiblock> MULTIBLOCK =
            Suppliers.memoize(() ->
                    PatchouliAPI.get().makeMultiblock(
                            new String[][] {
                                    {" S ", "S0S", " P "},
                                    {"SSS", "SSS", "SSS"}
                            },
                    'S', Blocks.STONE_BRICKS,
                            '0', BotaniaTheurgiaMod.EXAMPLE_BLOCK.get(),
                            'P', BotaniaBlocks.manaPool
                    )
            );


    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            PoorAltarCoreBlockEntity altar
    ) {
        if (level.isClientSide) return;

        Rotation rotation = MULTIBLOCK.get().validate(level, pos);
        if (rotation == null) return;

        BlockPos poolPos = pos.offset(
                new BlockPos(1, 0, 0).rotate(rotation)
        );

        if (!(level.getBlockEntity(poolPos) instanceof ManaPool pool)) {
            return;
        }

        AABB area = new AABB(pos)
                .inflate(0.25);

        for (ItemEntity entity :
                level.getEntitiesOfClass(ItemEntity.class, area)) {

            if (!entity.isAlive()) continue;

            ItemStack input = entity.getItem();
            if (input.isEmpty()) continue;

            // 4. レシピ検索
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, input.copy());

            var recipeOptional = level.getRecipeManager().getRecipeFor(
                    PoorAltarRecipes.TYPE.get(),
                    container,
                    level
            );

            if (recipeOptional.isEmpty()) continue;

            var recipe = recipeOptional.get();

            ItemStack output = recipe.assemble(
                    container,
                    level.registryAccess()
            );

            if (output.isEmpty()) continue;

            int manaCost = 100;

            if (pool.getCurrentMana() < manaCost) {
                continue;
            }

            input.shrink(1);

            if (input.isEmpty()) {
                entity.discard();
            } else {
                entity.setItem(input);
            }

            pool.receiveMana(-manaCost);

            ItemEntity result = new ItemEntity(
                    level,
                    pos.getX() + 0.5,
                    pos.getY() + 1.2,
                    pos.getZ() + 0.5,
                    output
            );

            result.setPickUpDelay(10);
            level.addFreshEntity(result);
        }
    }

}
