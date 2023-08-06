package roger;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class Util {
    public static void msg(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
    }

    public static BlockPos getPlayerBlockPos() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        BlockPos pos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));

        if(isBlockSolid(pos)) {
            if (isBlockSolid(pos = pos.add(0,1,0)))
                player.addChatMessage(new ChatComponentText("player block pos was solid! (cannot continue)"));
        }

        player.addChatMessage(new ChatComponentText(pos.toString()));
        return pos;
    }
    private static boolean isBlockSolid(BlockPos block) {
        return Minecraft.getMinecraft().theWorld.getBlockState(block)
                .getBlock().isBlockSolid(Minecraft.getMinecraft().theWorld, block, null) ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSlab ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockStainedGlass ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPane ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockFence ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonExtension ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockEnderChest ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockTrapDoor ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonBase ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockChest ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockStairs ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockCactus ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockWall ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockGlass ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSkull ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSand;
    }


}
