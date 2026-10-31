package com.shimgo2008.botaniathuergia.blocks;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.shimgo2008.botaniathuergia.BotaniaTheurgiaMod;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class PoorAltarCoreBlock extends Block {
    public static final Supplier<IMultiblock> MULTIBLOCK =
            Suppliers.memoize(() ->
                    PatchouliAPI.get().makeMultiblock(
                            new String[][] {
                                    {" S ", "S0S", " S "},
                                    {"SSS", "SSS", "SSS"}
                            },
                            'S', Blocks.STONE_BRICKS,
                            '0', BotaniaTheurgiaMod.EXAMPLE_BLOCK.get()
                    )
            );
    public PoorAltarCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        System.out.println("PoorAltarCoreBlock: Player " + player.getName().getString() + " interacted with block at " + pos);
        if (hand != net.minecraft.world.InteractionHand.MAIN_HAND) {
            System.out.println("PoorAltarCoreBlock: Ignoring offhand interaction");
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            System.out.println("PoorAltarCoreBlock: Server-side interaction, validating multiblock");
            Rotation rotation = MULTIBLOCK.get().validate(level, pos);

            if (rotation != null) {
                System.out.println("PoorAltarCoreBlock: Multiblock valid, rotating " + rotation);
                level.setBlockAndUpdate(
                        pos,
                        Blocks.END_PORTAL.defaultBlockState()
                );
            } else {
                System.out.println("PoorAltarCoreBlock: Multiblock invalid");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
