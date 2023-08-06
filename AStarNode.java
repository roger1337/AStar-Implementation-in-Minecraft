package roger.astar;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class AStarNode {
    private int hCost;
    private int gCost;

    private final int x;
    private final int y;
    private final int z;

    private AStarNode parent;
    private BlockPos blockPos;


    public AStarNode(BlockPos pos, AStarNode parentNode, AStarNode endNode) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.blockPos = pos;

        calculateHeuristic(endNode);
        setParent(parentNode);
    }

    public AStarNode(int xRel, int yRel, int zRel, AStarNode parentNode, AStarNode endNode) {
        this.x = xRel + parentNode.getX();
        this.y = yRel + parentNode.getY();
        this.z = zRel + parentNode.getZ();
        this.blockPos = new BlockPos(x, y, z);

        calculateHeuristic(endNode);
        setParent(parentNode);
    }

    public AStarNode(BlockPos pos, AStarNode endNode) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();

        calculateHeuristic(endNode);
    }

    public AStarNode(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public boolean canBeTraversed() {
        return !isBlockSolid(blockPos) && !isBlockSolid(new BlockPos(x, y+1, z));
    }

    private boolean isBlockSolid(BlockPos block) {
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
    private void calculateHeuristic(AStarNode endNode) {
        this.hCost = (Math.abs(endNode.getX() - x) + Math.abs(endNode.getY() - y) + Math.abs(endNode.getZ() - z)) * 10;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;

        int xDiff = Math.abs(x - parent.getX());
        int yDiff = Math.abs(y - parent.getY());
        int zDiff = Math.abs(z - parent.getZ());

        double xyHypotenuse = Math.sqrt(xDiff*xDiff + yDiff*yDiff);

        this.gCost = parent.getGCost() + (int) Math.floor(
                Math.sqrt(xyHypotenuse*xyHypotenuse + zDiff*zDiff) * 10
        );
    }

    public int getTotalCost() {
        return hCost + gCost;
    }

    public int getX() {
        return x;
    }

    public int getGCost() {
        return gCost;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public AStarNode getParent() {
        return parent;
    }

    public Vec3 asVec3(double xAdd, double yAdd, double zAdd) {
        return new Vec3(x + xAdd, y + yAdd, z + zAdd);
    }



    @Override
    public boolean equals(Object o) {
        if(!(o instanceof AStarNode))
            return false;

        AStarNode other = (AStarNode)o;

        return x == other.getX() && y == other.getY() && z == other.getZ();
    }
}


